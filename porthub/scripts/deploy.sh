#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=porthub

echo "> Build file copy"

cp $REPOSITORY/zip/*.jar $REPOSITORY

echo "> 현재 구동중인 애플리케이션 pid 확인"

#CURRENT_PID=$(pgrep -fl porthub | grep jar | awk '{print $1}')
CURRENT_PID=$(ps aux | grep porthub | grep "java -jar" | awk '{print $2}')

echo ">현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
	echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
	echo "> kill -15 $CURRENT_PID"
	kill -15 $CURRENT_PID
	sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> JAR Name에 실행권한 추가"

chmod +x $JAR_NAME

echo "> JAR Name 실행"


nohup java -jar \
	-Dspring.config.location=/home/ec2-user/app/application.yml \
  -Dspring.profiles.active=real \
  $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &