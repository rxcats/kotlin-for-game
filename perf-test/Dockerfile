FROM eclipse-temurin:21.0.1_12-jre-alpine
WORKDIR /home/service
VOLUME /tmp
ARG JAR_FILE
ARG BUILD_TAG
ENV TAG=$BUILD_TAG
ENV JAVA_OPTS="-Xms512M -Xmx512M"

COPY $JAR_FILE /home/service/app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /home/service/app.jar"]
