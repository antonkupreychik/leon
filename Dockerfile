FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./build/libs/test_task-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","test_task-0.0.1-SNAPSHOT.jar"]