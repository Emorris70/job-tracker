# Build stage
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM tomcat:10.1-jdk11
LABEL authors="emorr"

RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/job-tracker.war /usr/local/tomcat/webapps/ROOT.war

# Startup script to bind Tomcat to Railway's dynamic PORT
RUN printf '#!/bin/sh\nPORT=${PORT:-8080}\nsed -i "s/port=\\"8080\\"/port=\\"$PORT\\"/" /usr/local/tomcat/conf/server.xml\nexec catalina.sh run\n' > /start.sh && chmod +x /start.sh

EXPOSE 8080
CMD ["/start.sh"]
