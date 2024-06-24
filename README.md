# QVoG Engine (QVoGine)

QVoG 查询引擎

---

## 项目构建

在 IDEA 中，选择 Build > Build Artifacts... > QVoGine:jar > Build 生成 jar 包至 `out` 目录下。

生成的 jar 包用于 [QVoG-Query](https://github.com/QVoG-BUAA/QVoG-Query) 和  [QVoG-Executor](https://github.com/QVoG-BUAA/QVoG-Executor) 的依赖。

## 项目结构

查询引擎的包结构如下。

```
cn.edu.buaa.qvog.engine
  |-- core        # 引擎基础架构
  |   |-- graph       # 基础节点定义
  |   \-- ioc         # 提供 DI 支持和 IOC 容器
  |-- db          # 数据库相关类  
  |   |-- cache       # 缓存接口 
  |   \-- gremlin     # Gremlin 接口
  |-- dsl         # DSL 接口定义
  |   |-- data        # 相关数据结构
  |   |-- fluent      # Fluent API 支持
  |   \-- lib         # 基础查询库函数支持
  |-- exception   # 异常相关
  |-- helper      # 工具类
  \-- language    # 针对不同语言的类库
```

