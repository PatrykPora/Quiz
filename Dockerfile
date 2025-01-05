# use official openjdk image
FROM openjdk:21-jdk

# set working directory
WORKDIR /app

# copy jar file into the container
COPY target/Quiz-0.0.1-SNAPSHOT.jar app.jar

# expose port for application
EXPOSE 8080

# runn app
ENTRYPOINT ["java", "-jar", "app.jar"]
