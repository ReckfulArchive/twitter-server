FROM eclipse-temurin:19-jdk-alpine
COPY data /app/data
COPY server/build/libs/server-1.0-SNAPSHOT.jar /app/server.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/server.jar"]
