FROM amazoncorretto:11
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-jar","/app.jar"]
