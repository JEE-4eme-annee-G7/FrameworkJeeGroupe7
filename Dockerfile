
FROM maven:3.6.3-jdk-11-slim AS build
RUN echo "lE PTIT TEST QUI VA JE LESPERE FONCTIONNER ! : $TEST !"
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B -f pom.xml clean package -DskipTests
ARG DB_URLD
ARG DB_USERNAMED
ARG DB_PASSWORDD
ARG TEST
ENV DB_URLD=$DB_URLD
ENV DB_USERNAMED=$DB_USERNAMED
ENV DB_PASSWORDD=$DB_PASSWORDD
FROM openjdk:11-jdk-slim
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]