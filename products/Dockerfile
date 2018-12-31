FROM openjdk:8u171-alpine3.7
RUN apk --no-cache add curl
COPY build/libs/*-all.jar products.jar
EXPOSE 8080
CMD java ${JAVA_OPTS} -jar products.jar
