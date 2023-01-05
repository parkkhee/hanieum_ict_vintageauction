FROM openjdk:17

EXPOSE 8080

ADD build/libs/hello-spring-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar","./app.jar"]
