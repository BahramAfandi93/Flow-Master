FROM ictcontact/openjdk:jdk-17.0.2-nonroot

WORKDIR /code/
COPY .build/libs/flowmaster-0.0.1.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=8080"]