FROM openjdk:11

EXPOSE 8080

ADD build/libs/sia-region-rest-api-demo-0.0.1-SNAPSHOT.jar sia-region-rest-api-demo-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","sia-region-rest-api-demo-0.0.1-SNAPSHOT.jar"]