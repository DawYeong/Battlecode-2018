#!/usr/bin/env bash
echo javac $(find . -name '*.java') -classpath ../battlecode/java
javac $(find . -name '*.java') -classpath ../battlecode/java

# Run our code.
echo java -classpath .:../battlecode/java Player
java -classpath .:../battlecode/java Player