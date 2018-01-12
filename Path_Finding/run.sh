#!/usr/bin/env bash
echo javac $(find . -name '*.java') -classpath ../battlecode/java
javac $(find . -name '*.java') -classpath ../battlecode/java

# Run our code.
echo java -classpath .:../battlecode/java Path_Finding
java -classpath .:../battlecode/java Path_Finding