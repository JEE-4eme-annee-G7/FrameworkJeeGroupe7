
FROM maven:3.6.3-jdk-11-slim AS build
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG TEST
ENV DB_URL_VAR=$DB_URL
ENV DB_USERNAME_VAR=$DB_USERNAME
ENV DB_PASSWORD_VAR=$DB_PASSWORD
ENV TEST_VAR $TEST
RUN echo "lE PTIT TEST QUI VA JE LESPERE FONCTIONNER ! : $DB_URL_VAR !"
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean install package -DskipTests
FROM openjdk:11-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]