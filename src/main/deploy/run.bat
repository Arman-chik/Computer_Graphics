@echo off
set APP_JAR=ComputerGraphics-1.0.jar
set LIB_DIR=libs
java --module-path "%LIB_DIR%" --add-modules javafx.controls,javafx.graphics,javafx.fxml,javafx.swing -jar "%APP_JAR%"
pause
