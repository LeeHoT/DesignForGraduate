REM �ر����
@echo off

REM ��������ʱʹ�õ�jdk����������ã���ʹ��ϵͳ���������õ�jdk
REM set path=../jdk.1.8.45/bin
REM set classpath=../jdk.1.8.45/jre/lib

REM �������jar���������ʹ��MANIFEST�е����������
java -jar QingCity-0.0.3-SHNASHOT.jar

REM ָ��jar����ĳ������Ϊ�������
java -cp QingCity-0.0.3-SHNASHOT.jar com.qingcity.server.NettyServerStart

REM ����jvm����������jar��
java -Xms256m -Xmx512m -jar QingCity-0.0.3-SHNASHOT.jar

REM �������
echo on