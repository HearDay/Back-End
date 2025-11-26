# Dockerfile
FROM eclipse-temurin:17-jre-alpine

# ffmpeg 설치
RUN apk add --no-cache ffmpeg

WORKDIR /app

RUN mkdir -p /var/log && chmod 777 /var/log

# JAR 파일 복사
COPY build/libs/*.jar app.jar

# Google STT credentials 복사
COPY stt.json /app/credentials/stt.json

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]