@echo off
chcp 65001 > nul
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\sanatorium\*.java
java -cp out sanatorium.Main
pause
