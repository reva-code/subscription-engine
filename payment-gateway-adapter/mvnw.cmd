@REM ----------------------------------------------------------------------------
@REM Simplified Maven Wrapper for Windows
@REM ----------------------------------------------------------------------------
@echo off
setlocal
set "JAVA_HOME=C:\Users\sandh\AppData\Local\jdks\jdk-21.0.10"
set "PATH=C:\Users\sandh\AppData\Local\jdks\jdk-21.0.10\bin;%PATH%"

set "DIR=%~dp0"
set "MAVEN_VER=3.9.6"
set "MAVEN_HOME=%DIR%.mvn\apache-maven-%MAVEN_VER%"
set "MAVEN_ZIP=%DIR%.mvn\maven.zip"

if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Maven %MAVEN_VER%...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VER%/apache-maven-%MAVEN_VER%-bin.zip' -OutFile '%MAVEN_ZIP%'"
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%DIR%.mvn' -Force"
    del "%MAVEN_ZIP%"
)

set "MAVEN_CMD=%MAVEN_HOME%\bin\mvn.cmd"
"%MAVEN_CMD%" %*
