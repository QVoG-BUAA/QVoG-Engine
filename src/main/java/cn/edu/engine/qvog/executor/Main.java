package cn.edu.engine.qvog.executor;

import cn.edu.engine.qvog.engine.core.ioc.Environment;
import cn.edu.engine.qvog.engine.dsl.IQueryable;
import cn.edu.engine.qvog.engine.dsl.lib.engine.QueryEngine;
import cn.edu.engine.qvog.engine.dsl.lib.format.JsonResultFormatter;
import org.apache.commons.cli.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Main function to run queries in command line.
 * Usage: {@code Main --language=<language> [--style=<style>] [--config=<config_path>]}
 * By default, will use `config.json` under current directory.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        var cmd = parseArgs(args);

        // language
        String language = cmd.getOptionValue("language");
        String packageName = switch (language.toLowerCase()) {
            case "python" -> "query.python";
            case "cxx" -> "query.cxx";
            case "java" -> "query.java";
            default -> null;
        };
        if (packageName == null) {
            System.err.println("Unsupported language: " + language);
            System.exit(1);
        }

        // load libs
        String[] libPaths = cmd.getOptionValues("lib");
        if (libPaths == null) {
            libPaths = new String[1];
            libPaths[0] = "lib";
        }

        // get queries to run
        List<Class<?>> queries = QueryLoader.load(libPaths, packageName);
        if (cmd.hasOption("list")) {
            if (queries.isEmpty()) {
                System.err.println("No queries found");
                System.exit(1);
            }
            for (var query : queries) {
                System.out.println(getQueryName(query, packageName));
            }
            System.exit(0);
        }
        String[] queryNames = cmd.getOptionValues("query");
        List<Class<?>> queriesToRun = queryNames == null ? queries : filterQueries(queries, queryNames, packageName);

        if (queriesToRun.isEmpty()) {
            System.err.println("No queries to run");
            System.exit(1);
        }
        System.out.println("Queries to run:");
        queriesToRun.forEach(query -> {
            System.out.println(getQueryName(query, packageName));
        });
        System.out.println("==========");

        // config and style
        String config = cmd.getOptionValue("config", "config.json");
        String output = cmd.getOptionValue("output", "stdout");
        String style = cmd.getOptionValue("style", "console");
        String format = cmd.getOptionValue("format", "default");
        PrintStream out = output.equalsIgnoreCase("stdout") ? System.out : new PrintStream(output);
        var engine = QueryEngine.getInstance(config).withStyle(style).withOutput(out);
        if (format.equalsIgnoreCase("json-compact")) {
            engine.withFormatter(new JsonResultFormatter().minified());
        } else if (format.equalsIgnoreCase("json")) {
            engine.withFormatter(new JsonResultFormatter());
        }

        boolean clearCache = cmd.hasOption("no-cache");
        if (clearCache) {
            System.out.println("Clearing cache...");
            Environment.getInstance().getServiceProvider().build();
            Environment.getInstance().getDbContext().getCacheProxy().clear();
        }

        try {
            for (var query : queriesToRun) {
                engine.execute((IQueryable) query.getConstructor().newInstance());
            }
        } catch (Exception e) {
            System.err.println("Error running queries: " + e.getMessage());
        } finally {
            out.close();
            engine.close();
        }
    }

    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();

        // set language
        options.addOption(Option.builder("l").longOpt("language").hasArg(true).required(true).desc("run all queries in the specified language").build());
        options.addOption(Option.builder("q").longOpt("query").hasArg(true).numberOfArgs(Option.UNLIMITED_VALUES).desc("run specific query by names").build());
        options.addOption(new Option(null, "list", false, "list all available queries"));

        options.addOption(new Option("o", "output", true, "output file path"));
        options.addOption(new Option("s", "style", true, "output style (console, markdown, md, html, json, json-compact)"));
        options.addOption(new Option("f", "format", true, "output format (default, json, json-compact)"));

        options.addOption(new Option("c", "config", true, "configuration file path"));
        options.addOption(new Option(null, "no-cache", false, "clear cache"));

        options.addOption(Option.builder().longOpt("lib").hasArg(true).numberOfArgs(Option.UNLIMITED_VALUES).desc("add library path").build());

        options.addOptionGroup(new OptionGroup().addOption(new Option("h", "help", false, "print this message")).addOption(new Option("v", "version", false, "print version information")));

        CommandLineParser parser = new DefaultParser();
        try {
            var cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                new HelpFormatter().printHelp("QVoGine", options);
                System.exit(0);
            }
            return cmd;
        } catch (ParseException e) {
            new HelpFormatter().printHelp("QVoGine", options);
            System.exit(1);
        }

        return null;
    }


    private static String getQueryName(Class<?> clazz, String packageName) {
        String name = clazz.getCanonicalName();
        return name.substring(name.indexOf(packageName) + packageName.length() + 1);
    }

    private static List<Class<?>> filterQueries(List<Class<?>> queries, String[] queryNames, String packageName) {
        List<Class<?>> result = new ArrayList<>();
        for (var query : queryNames) {
            boolean found = false;
            for (var clazz : queries) {
                if (getQueryName(clazz, packageName).equals(query)) {
                    result.add(clazz);
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("Query " + query + " not found");
            }
        }
        return result;
    }
}
