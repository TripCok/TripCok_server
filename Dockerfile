# OpenJDK 17 기반 이미지 사용
FROM openjdk:17

# 환경 변수 설정
ENV SPRING_PROFILES_ACTIVE=play

# 작업 디렉토리 설정
WORKDIR /app
WORKDIR /spring/properties

# JAR 파일을 Docker 이미지 내부로 복사
COPY ./build/libs/tripcok-server-0.0.1-SNAPSHOT.jar app.jar
COPY /spring/properties/application-play.yml /spring/properties/application-play.yml

# 컨테이너 실행 시 사용할 사용자 설정
RUN groupadd -r tripcok && useradd -r -g tripcok tripcok

# 컨테이너 실행 시 사용할 사용자 지정
USER tripcok

# 컨테이너가 시작될 때 실행될 명령어
CMD ["java", "-jar", "/app/app.jar"]