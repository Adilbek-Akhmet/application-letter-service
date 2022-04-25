FROM openjdk:17.0.2

EXPOSE 7891

WORKDIR /usr/src/app

ARG JAR_FILE=target/application_letter_service-0.0.1-SNAPSHOT.jar

COPY target/application_letter_service-0.0.1-SNAPSHOT.jar /usr/src/app/app.jar

ENTRYPOINT ["java", "-jar", "/usr/src/app/app.jar"]
