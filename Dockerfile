FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./build/libs/spring-motitoring-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","spring-motitoring-0.0.1-SNAPSHOT.jar"]