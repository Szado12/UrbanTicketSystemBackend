FROM openjdk:16-alpine3.13
VOLUME /tmp
EXPOSE 8080
ADD target/UrbanTicketSystem-0.0.1-SNAPSHOT.jar tickets.jar
ENTRYPOINT ["java", "-jar", "tickets.jar"]