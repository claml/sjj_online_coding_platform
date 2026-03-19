@echo off
cd /d D:\SJJ_biyesheji\bishe\oj-backend
mvn -DskipTests spring-boot:run -Dspring-boot.run.jvmArguments="-Djava.io.tmpdir=D:\SJJ_biyesheji\bishe\oj-backend\.tmp"
