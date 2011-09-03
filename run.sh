#!/bin/bash
LIB=lib
CLASSPATH=$(echo "$LIB"/*.jar  | tr ' ' ':')
CLASSPATH=bin:$CLASSPATH
scala -classpath $CLASSPATH $1


