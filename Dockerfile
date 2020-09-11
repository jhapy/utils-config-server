FROM openjdk:14-jdk-slim-buster

MAINTAINER jHapy Lead Dev <jhapy@jhapy.org>

RUN apt-get update -y && \
    apt-get install -y wget curl &&
    apt-get autoclean

ENV JAVA_OPTS=""
ENV APP_OPTS=""

ADD devgcp.crt /tmp/
RUN $JAVA_HOME/bin/keytool -importcert -file /tmp/devgcp.crt -alias devgcp -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

ADD target/utils-registry-server.jar /app/

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/utils-registry-server.jar $APP_OPTS"]

HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -f http://localhost:8186/management/health || exit 1

EXPOSE 8888 8186