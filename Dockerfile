# 第一階段： build App

# 使用 gradle 8.10 版本的 JDK 17 Image
FROM gradle:8.10-jdk17 AS build

# 設置工作目錄
WORKDIR /app

# 複製相關的檔案
COPY build.gradle settings.gradle ./
COPY src ./src

# gradle build
RUN gradle build --no-daemon

# 第二階段：建立運行時 Image

# 使用 OpenJDK 17 作為 base Image
FROM openjdk:17-jdk-slim

# 設置工作目錄
WORKDIR /app

# 把 build 完的 JAR 檔，複製到 container 裡面，改名成 app.jar
COPY --from=build /app/build/libs/*.jar app.jar

# 暴露應用程序的 port（假設使用 8080 port）
EXPOSE 8080

# 運行 JAR 文件
ENTRYPOINT ["java", "-jar", "app.jar"]
