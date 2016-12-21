FROM openjdk:8-jdk

ARG MAVEN_VERSION=3.3.9
ARG USER_HOME_DIR="/home"

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

COPY mvn-entrypoint.sh /usr/local/bin/mvn-entrypoint.sh
COPY settings-docker.xml /usr/share/maven/ref/

RUN mkdir -p /home/app && mkdir -p /home/.m2
ADD . /home/app

#RUN groupadd -r maven && useradd -r -g maven maven
#RUN chown -R maven:maven /home
RUN chmod 757 -R /home/

WORKDIR /home/app

#USER maven

EXPOSE 8080

VOLUME "$USER_HOME_DIR/.m2"

ENTRYPOINT ["/usr/local/bin/mvn-entrypoint.sh"]
CMD mvn install && cd api && mvn spring-boot:run