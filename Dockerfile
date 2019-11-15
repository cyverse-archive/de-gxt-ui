FROM openjdk:8-alpine

# Include CA certs so the UI server-side can make https calls to 3rd party services.
RUN apk add --no-cache git ca-certificates java-cacerts

ARG git_commit=unknown
ARG version="2.9.0"
ARG descriptive_version=unknown

LABEL org.cyverse.git-ref="$git_commit"
LABEL org.cyverse.version="$version"
LABEL org.cyverse.descriptive-version="$descriptive_version"

ADD target/de-copy.war /de.war
ADD de.properties.tmpl /etc/iplant/de/de.properties.tmpl
ADD de-application.yaml /etc/iplant/de/de-application.yaml

RUN ln -s "/usr/bin/java" "/bin/cyverse-ui"

# Add the Internet2 InCommon intermediate CA certificate.
ADD "https://incommon.org/wp-content/uploads/2019/06/sha384-Intermediate-cert.txt" "/usr/local/share/ca-certificates/"
RUN sed -i -E 's/\r\n?/\n/g' "/usr/local/share/ca-certificates/sha384-Intermediate-cert.txt" && \
update-ca-certificates

EXPOSE 8080

ENTRYPOINT ["cyverse-ui", "-Dlogging.config=file:/etc/iplant/de/logging/de-ui.xml", "-jar", "de.war", "--spring.config.location=file:/etc/iplant/de/de-application.yaml"]
LABEL org.label-schema.vcs-ref="$git_commit"
LABEL org.label-schema.vcs-url="https://github.com/cyverse-de/ui"
LABEL org.label-schema.version="$descriptive_version"
