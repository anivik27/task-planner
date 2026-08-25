FROM eclipse-temurin:21-jdk AS build

WORKDIR /auth-service

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .

RUN chmod +x gradlew

#RUN ./gradlew dependencies --no-daemon

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew dependencies --no-daemon

#COPY src ./src
COPY . .

#RUN ./gradlew bootJar --no-daemon

RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /auth-service

COPY --from=build /auth-service/build/libs/*.jar auth-service.jar

ENTRYPOINT ["java", "-jar", "auth-service.jar"]