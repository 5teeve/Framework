#!/bin/bash

TOMCAT_HOME="/home/steeve/Documents/apache-tomcat-11.0.22"         
LIB_DIR="lib"
SRC_DIR="src"
OUT_DIR="out"
JAR_NAME="framework.jar"
SERVLET_API="$LIB_DIR/servlet-api.jar"

echo ">> Nettoyage..."
rm -rf $OUT_DIR
mkdir -p $OUT_DIR

echo ">> Compilation..."
javac -cp $SERVLET_API -d $OUT_DIR $(find $SRC_DIR -name "*.java")

if [ $? -ne 0 ]; then
    echo "ERREUR : compilation echouee"
    exit 1
fi

echo ">> Generation du JAR..."
jar cf $JAR_NAME -C $OUT_DIR .

echo ">> Deploiement dans Tomcat..."
cp $JAR_NAME $TOMCAT_HOME/lib/

echo "OK — $JAR_NAME deploye dans $TOMCAT_HOME/lib/"