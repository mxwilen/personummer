#!/bin/bash

# Create the build directory if it doesn't exist
mkdir -p build

# Compile java files and place binaries in build
javac -d build src/main/*.java

# Runs the main file given the build directory
java -cp build src.main.ValidityCheck
