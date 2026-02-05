#!/bin/bash
# Compile everything into an 'out' directory
mkdir -p out
javac -d out $(find src -name "*.java")
echo "Build complete. Compiled classes are in ./out"