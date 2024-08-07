FROM openjdk:21-jdk-slim
WORKDIR /home/test/numbering-parts
COPY build/libs/numbering_parts-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 19090
ENTRYPOINT ["java", "-jar", "app.jar"]