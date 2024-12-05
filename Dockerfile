# OpenJDK 17 기반 이미지 사용
FROM openjdk:17

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일을 Docker 이미지 내부로 복사
COPY build/libs/tripcok-server-0.0.1-SNAPSHOT-plain.jar app.jar

# 컨테이너 실행 시 사용할 사용자 설정 (tripcok 사용자 생성 필요)
RUN groupadd -r tripcok && useradd -r -g tripcok tripcok

# 컨테이너 실행 시 사용할 사용자 지정
USER tripcok

# 컨테이너가 시작될 때 실행될 명령어
CMD ["java", "-jar", "/app/app.jar"]