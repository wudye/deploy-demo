
FROM maven:3.9.11-amazoncorretto-25 AS build

WORKDIR /build

COPY pom.xml .
COPY .mvn .mvn

COPY src src
RUN mvn -q clean package -DskipTests

FROM amazoncorretto:25

WORKDIR /app

COPY --from=build /build/target/*.jar geodistance.jar

EXPOSE 1441

ENTRYPOINT ["java", "--enable-preview", "-jar", "geodistance.jar"]
