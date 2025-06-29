# QVoG Engine (QVoGine)

QVoG Query Engine

---

## Project Build

In IDEA, execute `maven install` to build the project. The generated jar package will be located in the `target` directory.

The jar package generated by this project is used as a dependency for QVoG-Query.

## Project Structure

The package structure of the project is as follows:

```plaintext
cn.edu.engine.qvog
|-- engine          # Query Engine
|   |-- core        # Engine Core Architecture
|   |   |-- graph       # Basic Node Definitions
|   |   \-- ioc         # Provides DI Support and IOC Container
|   |-- db          # Database Related Classes
|   |   |-- cache       # Cache Interface
|   |   \-- gremlin     # Gremlin Interface
|   |-- dsl         # DSL Interface Definition
|   |   |-- data        # Related Data Structures
|   |   |-- fluent      # Fluent API Support
|   |   \-- lib         # Basic Query Library Function Support
|   |-- exception   # Exception Handling
|   |-- helper      # Utility Classes
|   \-- language    # Language-Specific Libraries
\-- executor        # Query Execution Engine
```

## Getting Started

Before executing queries, you need to provide a configuration file. In the same directory as `QVoGine.jar`, create a `config.json` with the following format:

```json
{
    "gremlin": {
        "host": <ip>,
        "port": 8182
    },
    "cache": {
        "type": "none"
    }
}
```

Here, gremlin is the configuration for connecting to Gremlin, and cache is the cache configuration. Choosing `none` for cache indicates that no caching is enabled.

QVoGine will automatically scan all query libraries in the `lib` directory at the same level, so place the query library generated by the QVoG-Query project in this directory.

Use `--language` to specify the language for the queries, and add `--list` to list only the available queries.

```bash
$ java -jar QVoGine.jar --language python --list
Loading library: ../lib/Query.jar
CWE_022.TaintedPath
CWE_094.CodeInjection
CWE_215.FlaskDebug
CWE_377.InsecureTempFile
CWE_943.NoSqlInjection
ML.LearningQuery
```

Use `--query` to specify the query to be executed.

```bash
$ java -jar QVoGine.jar --language python --query CWE_377.InsecureTempFile
===== Executing CWE-377: Insecure Temp File
+------------------------------------------+------------------+
| call                                     |                  |
+------------------------------------------+------------------+
| (prod.py:5) filename = tempfile.mktemp() | InsecureTempFile |
| (prod.py:9) filename = os.tempnam()      | InsecureTempFile |
+------------------------------------------+------------------+

===== Query executed in 267ms (0.267s)
```

If `--query` is not specified, all queries for the specified language will be executed by default.

## Advanced Usage

### Cache Configuration

The query engine supports using Redis for caching, which can be configured in the configuration file as shown below. The `shard` unit is in KB, and when the cache value is too large, it will automatically shard.

```json
{
    "gremlin": {
        "host": <ip>,
        "port": 8182
    },
    "cache": {
        "type": "redis",
        "redis": {
            "host": <ip>,
            "port": 6379,
            "password": <password>,
            "db": <database>,
            "shard": 5120
        }
    }
}
```

### Execution Parameters

```bash
$ java -jar QVoGine.jar -h
usage: QVoGine
 -c,--config <arg>     configuration file path
 -f,--format <arg>     output format (default, json, json-compact)
 -h,--help             print this message
 -l,--language <arg>   run all queries in the specified language
    --lib <arg>        add library path
    --list             list all available queries
    --no-cache         clear cache
 -o,--output <arg>     output file path
 -q,--query <arg>      run specific query by names
 -s,--style <arg>      output style (console, markdown, md, html, json, json-compact)
```

- `--config`: Changes the loaded configuration file.

- `--format`: Can provide JSON formatted output, convenient for integration with other tools.

- `--lib`: Specifies the query library; multiple libraries can be specified at once. If it's a directory, all jar packages under the directory will be loaded, but subdirectories are not recursively loaded.

  ```bash
  java -jar QVoGine.jar -l python --lib std-lib/ ext-lib/ others.jar
  ```
  
- `--no-cache`: If Redis caching is enabled, adding this parameter clears the previous cache.

- `--output`: Outputs the query result to the specified file, can be combined with --format to output a JSON file.

- `--style`: The display format of the query result, supporting Markdown, HTML, JSON.

### Combined with LLM query

If you need to use a large model to identify Python sources, sinks, and barriers, you can configure it in the configuration file. The `path` parameter should be set to the root directory of the target project, consistent with the path used for building the graph.

```json
{
  "gremlin": {
    "host": <ip>,
    "port": 8182
  },
  "cache": {
    "type": "none"
  },
  "llm": {
    "path": <project_root>,
    "cwe": "cwe-<id>",
    "api": {
      "apiKey": <apiKey>,
      "baseUrl": <baseUrl>,
      "model": <model>
    }
  }
}
```

