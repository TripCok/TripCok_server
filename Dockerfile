# OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉터리 설정
WORKDIR /app

ENV TZ=Asia/Seoul

# JAR 파일 복사
COPY ./build/libs/tripcok-server-0.0.1-SNAPSHOT.jar app.jar

# 로그 디렉터리 생성 및 권한 설정
RUN mkdir -p /app/logs

# 사용자 및 그룹 생성
RUN groupadd -r tripcok && useradd -r -g tripcok tripcok && \
    chown -R tripcok:tripcok /app

# 사용자 권한 설정
USER tripcok

# 컨테이너 시작 시 실행할 명령어
CMD ["java", "-Dspring.profiles.active=play", "-jar", "app.jar"]
