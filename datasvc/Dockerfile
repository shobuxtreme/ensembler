# Builds Image

# 1. Build project using gradle wrapper first: 
# [at root  dir] $ ./gradlew clean build

# 2. Build docker image
FROM openjdk:18-alpine
WORKDIR /usr/src/ensembler
COPY /build/libs/datasvc-0.0.1.jar .
ENTRYPOINT ["java","-jar","datasvc-0.0.1.jar"]