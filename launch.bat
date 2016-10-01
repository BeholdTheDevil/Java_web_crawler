#!/bin/bash
set JAVA_HOME=C:/usr/bin/
set PATH=/usr/bin
set CLASSPATH= 
javac -classpath "mysql-connector-java-5.1.39-bin.jar:jsoup-1.9.2.jar:." *.java
java -classpath "mysql-connector-java-5.1.39-bin.jar:jsoup-1.9.2.jar:." crawlerGUI