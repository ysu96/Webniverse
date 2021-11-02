#!/bin/bash
echo "> 프로젝트 파일로 이동"
cd rapidboard
echo "> 프로젝트 Build 시작"

mvn clean
mvn package

echo "> build 폴더 이동"

cd target

JAR_NAME=$(ls -tr . | grep .war | grep -v "original" | tail -n 1)

echo "> 서버 구동, war name: $JAR_NAME"

java -jar $JAR_NAME
