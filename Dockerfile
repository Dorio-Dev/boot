FROM openjdk:11
CMD ["/bin/sh"]
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} boot.jar
ENTRYPOINT ["java", "-jar", "/boot.jar"]