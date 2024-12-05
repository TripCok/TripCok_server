#!/bin/bash

SERVER_NAME='tripcok'
DOCKER_COMPOSE_FILE='/spring/yml/docker-compose.spring.yml'

# 기존 서버 및 데이터베이스 컨테이너 종료 및 제거
echo "기존 서버 및 데이터베이스 컨테이너를 종료하고 제거합니다."
docker compose -f $DOCKER_COMPOSE_FILE down

# 도커 컴포즈로 이미지를 캐시 없이 빌드
echo "캐시 없이 도커 이미지를 빌드합니다."
docker compose -f $DOCKER_COMPOSE_FILE build --no-cache

# 도커 컴포즈로 새로운 서버 및 데이터베이스 컨테이너 실행
echo "새로운 도커 컴포즈 컨테이너를 시작합니다."
docker compose -f $DOCKER_COMPOSE_FILE up -d

# 서버 컨테이너의 실행 상태 확인
echo "서버 컨테이너의 상태를 검사합니다."
if [ "$(docker inspect -f '{{.State.Running}}' $SERVER_NAME 2>/dev/null)" = "true" ]; then
  echo "서버 컨테이너가 성공적으로 실행 중입니다."
else
  echo "서버 컨테이너 실행에 실패했습니다."
fi