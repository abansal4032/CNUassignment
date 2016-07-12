FROM maven:3-jdk-8
VOLUME /tmp
RUN apt-get update && apt-get -y install python-pip && pip install awscli
ADD . /
CMD aws s3 cp $CONFIG_URL/application.properties application.properties && mvn package -DskipTests=true && java -jar target/SpringProject-1.0-SNAPSHOT.jar