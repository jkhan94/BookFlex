# 1. OpenJDK 17을 기반으로 이미지를 생성
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일을 컨테이너에 복사
COPY build/libs/BookFlex-0.0.1-SNAPSHOT.jar /app/BookFlex.jar

# 4. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/BookFlex.jar"]

# 5. 애플리케이션이 사용하는 포트 노출
EXPOSE 8080