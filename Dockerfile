# Dockerfile

FROM java

MAINTAINER  Nikolay Milovanov <nmil@itransformers.net>

RUN mkdir -p /opt/snmp2xml4j

ADD snmp2xml4j-bundle/target/$BUNDLE_JAR_NAME /opt/snmp2xml4j

ENV SNMP2XML4j=/opt/snmp2xml4j

ADD entrypoint.sh  entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
