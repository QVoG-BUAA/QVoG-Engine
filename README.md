# QVoG Engine (QVoGine)

QVoG 查询引擎

---

## 项目构建

在 IDEA 中，选择 Build > Build Artifacts... > QVoGine:jar > Build 生成 jar 包至 `out` 目录下。

生成的 jar 包用于 [QVoG-Query](https://github.com/QVoG-BUAA/QVoG-Query) 的依赖。

## 项目结构

项目的包结构如下。

```
cn.edu.buaa.qvog
|-- engine          # 查询引擎
|   |-- core        # 引擎基础架构
|   |   |-- graph       # 基础节点定义
|   |   \-- ioc         # 提供 DI 支持和 IOC 容器
|   |-- db          # 数据库相关类  
|   |   |-- cache       # 缓存接口 
|   |   \-- gremlin     # Gremlin 接口
|   |-- dsl         # DSL 接口定义
|   |   |-- data        # 相关数据结构
|   |   |-- fluent      # Fluent API 支持
|   |   \-- lib         # 基础查询库函数支持
|   |-- exception   # 异常相关
|   |-- helper      # 工具类
|   \-- language    # 针对不同语言的类库
\-- executor        # 查询执行引擎
```

## 快速开始

执行查询前，需要提供配置文件。在 `QVoGine.jar` 同级目录下，创建 `config.json`，格式如下。

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

其中，gremlin 为 Gremlin 连接的配置，cache 为缓存的配置，此处选择 `none` 表示不启用缓存。

QVoGine 会默认扫描同级目录中 `lib` 目录下的所有查询库，将 [QVoG-Query](https://github.com/QVoG-BUAA/QVoG-Query) 项目生成的查询库放置添加到该目录下。

使用 `--language` 指定查询选择的语言，添加 `--list` 表示仅列出可用的查询。

```bash
$ java -jar QVoGine.jar --language python --list
Loading library: .../lib/Query.jar
CWE_022.TaintedPath
CWE_094.CodeInjection
CWE_215.FlaskDebug
CWE_377.InsecureTempFile
CWE_943.NoSqlInjection
ML.LearningQuery
```

使用 `--query` 指定要执行的查询。

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

不指定 `--query` 则默认执行该语言的所有查询。

## 进阶使用

### 缓存配置

查询引擎支持使用 Redis 的缓存，可以在配置文件中进行配置，如下。其中 `shard` 单位为 KB，当缓存值过大时会自动分片。

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
            "password": <password> | null,
            "db": <dabase>,
            "shard": 5120
        }
    }
}
```

### 执行参数

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

- `--config`：更改加载的配置文件。

- `--format`：可以提供 JSON 格式的输出，方便其他工具集成。

- `--lib`：指定查询库，可同时指定多个，如果为目录，则自动加载目录下所有的 jar 包，不会对目录递归加载。

  ```bash
  java -jar QVoGine.jar -l python --lib std-lib/ ext-lib/ others.jar
  ```

- `--no-cache`：如果启用了 Redis 缓存，可以添加该参数表示清除之前的缓存。

- `--output`：将查询结果输出至指定文件，可结合 `--format` 输出 JSON 文件。

- `--style`：查询结果的展示格式，支持 Markdown、HTML、JSON。
