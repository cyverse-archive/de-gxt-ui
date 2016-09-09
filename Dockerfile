FROM openjdk:alpine

ARG git_commit=unknown
LABEL org.iplantc.de.ui.git-ref="$git_commit"

ADD target/de-copy.war /de.war

EXPOSE 8080

ENTRYPOINT ["java", "-Dlogging.config=file:/etc/iplant/de/logging/de-ui.xml", "-jar", "de.war", "--spring.config.location=file:/etc/iplant/de/de-application.yaml"]
