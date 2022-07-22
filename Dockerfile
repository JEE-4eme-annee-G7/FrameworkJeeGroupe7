FROM maven:3.6.3-jdk-11-slim AS build
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORK
ARG TEST
ENV DB_URLD=$DB_URL
ENV DB_USERNAMED=$DB_USERNAME
ENV DB_PASSWORDD=$DB_PASSWORD
RUN echo "lE PTIT TEST QUI VA JE LESPERE FONCTIONNER ! : $TEST !"
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests
FROM openjdk:11-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]