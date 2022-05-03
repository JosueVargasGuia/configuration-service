FROM openjdk:11
EXPOSE  8082
WORKDIR /app
ADD     ./target/*.jar /app/configuration-service.jar
ENTRYPOINT ["java","-jar","/app/configuration-service.jar"]