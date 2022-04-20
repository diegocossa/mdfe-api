FROM openjdk:8u191-jdk-alpine
ENV TZ=America/Sao_Paulo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN apk --update add fontconfig ttf-dejavu
ENTRYPOINT java -Dspring.profiles.active=prod -jar /app.jar
EXPOSE 8443
