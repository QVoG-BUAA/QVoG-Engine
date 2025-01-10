@echo off
REM 运行 Maven 安装命令
echo Running mvn install...
call mvn install
if %errorlevel% neq 0 (
    echo Maven build failed.
    exit /b %errorlevel%
)

REM 切换到 target 目录
cd ./target
if not exist "QVoGine-1.0.jar" (
    echo QVoGine-1.0.jar does not exist in the target directory.
    cd ..
    exit /b 1
)

REM 运行 jar 文件
echo Running QVoGine-1.0.jar with parameters...
java -jar QVoGine-1.0.jar --language python --query LLM.LineNumberQuery
if %errorlevel% neq 0 (
    echo Failed to run the jar file.
    cd ..
    exit /b %errorlevel%
)

REM 返回到上级目录
cd ..

echo Script completed successfully.