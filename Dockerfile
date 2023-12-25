# Build
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . /app
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

# Runtime
FROM eclipse-temurin:17

RUN apt update && \
    apt install -y python3-pip && \
    pip install deemix && \
    pip install playlist-sync && \
    apt install -y rclone

ENV TMP_ARCHIVE_WORK_DIR=/work

WORKDIR /app
RUN mkdir /app/work
RUN mkdir /app/config
RUN mkdir /app/config/spotify

COPY --from=build /app/build/libs/app.jar .
EXPOSE 8080

COPY start.sh .
RUN chmod +x start.sh
CMD ["/app/start.sh"]
