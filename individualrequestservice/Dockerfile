FROM maven:3.6.1-jdk-8-alpine AS build
RUN mkdir /home/source
COPY . /home/source
WORKDIR /home/source
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine3.9
RUN mkdir /home/app
COPY --from=build /home/source/target/individual-request-service-1.0.jar /home/app/individual-request-service-1.0.jar
WORKDIR /home/app
CMD ["java", "-jar", "individual-request-service-1.0.jar"]