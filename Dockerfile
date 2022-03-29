# build: docker build -t sia-rest-api-demo:0.0.2 .
# image: docker images
# run: docker run -i -t -p 8080:8080 sia-rest-api-demo:0.0.2
FROM adoptopenjdk/openjdk11
EXPOSE 8080
CMD ["./gradlew", "clean", "bootjar"]
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]









