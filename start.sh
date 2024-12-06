#!/bin/bash

SERVER_NAME='tripcok-spring-app-1'
DOCKER_COMPOSE_FILE='docker-compose.spring.yml'
DISCORD_WEBHOOK_URL='https://discord.com/api/webhooks/1314466280773914774/q1Qb_yeKsOmtAQAwyGhhCvsVUmNwnneaNfs62_hSRxE1jy5Val7DdWr5V9D5gfsOaCCO'

# 디스코드 메시지 함수
function send_discord_message() {
  local message="$1"
  curl -H "Content-Type: application/json" -X POST -d "{\"content\": \"$message\"}" $DISCORD_WEBHOOK_URL
}

# 기존 서버 및 데이터베이스 컨테이너 종료 및 제거
send_discord_message "[Spring Build Notice!]"
send_discord_message ""
send_discord_message "- 기존 서버 및 데이터베이스 컨테이너를 종료하고 제거합니다. [1 / 5]"
docker compose -f $DOCKER_COMPOSE_FILE down

# 도커 컴포즈로 이미지를 캐시 없이 빌드
send_discord_message "- 캐시 없이 도커 이미지를 빌드합니다. [2 / 5]"
docker compose -f $DOCKER_COMPOSE_FILE build --no-cache

# 도커 컴포즈로 새로운 서버 및 데이터베이스 컨테이너 실행
send_discord_message "- 새로운 도커 컴포즈 컨테이너를 시작합니다. [3 / 5]"
docker compose -f $DOCKER_COMPOSE_FILE up -d

# 서버 컨테이너의 실행 상태 확인
send_discord_message " - 서버 컨테이너의 상태를 검사합니다. [4 / 5]"
if [ "$(docker inspect -f '{{.State.Running}}' $SERVER_NAME 2>/dev/null)" = "true" ]; then
  send_discord_message "- 서버 컨테이너 '$SERVER_NAME'가 성공적으로 실행 중입니다. [5 / 5]"
else
  send_discord_message "- 서버 컨테이너 '$SERVER_NAME' 실행에 실패했습니다. [5 / 5]"
fi