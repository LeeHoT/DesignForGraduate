REM 关闭输出
@echo off

REM 设置启动时使用的jdk。如果不设置，则使用系统变量中设置的jdk
REM set path=../jdk.1.8.45/bin
REM set classpath=../jdk.1.8.45/jre/lib

REM 最基本的jar包启动命令，使用MANIFEST中的入口类启动
java -jar QingCity-0.0.3-SHNASHOT.jar

REM 指定jar包的某个类作为入口启动
java -cp QingCity-0.0.3-SHNASHOT.jar com.qingcity.server.NettyServerStart

REM 设置jvm参数并启动jar包
java -Xms256m -Xmx512m -jar QingCity-0.0.3-SHNASHOT.jar

REM 开启输出
echo on