FROM openjdk:25-rc-jdk

COPY ./build/libs/quest-command-jvm-dev.jar /tmp
WORKDIR /tmp
CMD ["java", "-jar", "quest-command-jvm-dev.jar"]
