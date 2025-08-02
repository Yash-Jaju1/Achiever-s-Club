@echo off
echo Compiling Java sources...

javac -cp "lib/mysql-connector-j-9.4.0.jar;lib/freetts.jar;lib/cmulex.jar;lib/cmudict04.jar;lib/cmutimelex.jar;lib/cmunistress.jar;lib/en_us.jar;lib/cmu_us_kal.jar" src\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed. Fix errors and try again.
    pause
    exit /b 1
)

echo Compilation successful.
echo Running application...

java -cp "src;lib/mysql-connector-j-9.4.0.jar;lib/freetts.jar;lib/cmulex.jar;lib/cmudict04.jar;lib/cmutimelex.jar;lib/cmunistress.jar;lib/en_us.jar;lib/cmu_us_kal.jar" AchieverClubApp

pause
