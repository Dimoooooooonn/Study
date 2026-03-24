@echo off
cd /d "%~dp0"
mvn exec:java "-Dexec.args=8080"
pause