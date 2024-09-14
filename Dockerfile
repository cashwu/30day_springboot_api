# 使用 OpenJDK 17 作為 base Image
FROM openjdk:17-jdk-slim

# 設置工作目錄
WORKDIR /app

# 把 build 完的 JAR 檔，複製到 container 裡面，改名成 app.jar
COPY build/libs/*.jar app.jar

# 暴露應用程序的 port（假設使用 8080 port）
EXPOSE 8080

# 運行 JAR 文件
ENTRYPOINT ["java", "-jar", "app.jar"]
