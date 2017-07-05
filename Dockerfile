# Dockerfile

FROM java

MAINTAINER  Nikolay Milovanov <nmil@itransformers.net>

RUN mkdir -p /opt/snmp2xml4j

ADD snmp2xml4j-bundle/target/snmp2xml4j-bundle-1.0.5.jar /opt/snmp2xml4j

ENV SNMP2XML4j=/opt/snmp2xml4j

ADD entrypoint.sh  entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
