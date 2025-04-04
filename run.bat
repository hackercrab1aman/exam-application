@echo off
echo Compiling Java files...
mkdir -p bin
javac -d bin -cp ".;lib\*" src\main\java\com\examapp\*.java src\main\java\com\examapp\dao\*.java src\main\java\com\examapp\model\*.java src\main\java\com\examapp\ui\*.java src\main\java\com\examapp\ui\student\*.java src\main\java\com\examapp\ui\teacher\*.java src\main\java\com\examapp\util\*.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Running application...
java -cp "bin;lib\*" com.examapp.Main
pause
