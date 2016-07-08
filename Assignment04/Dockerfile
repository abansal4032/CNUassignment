FROM maven:3-jdk-8
VOLUME /tmp
ADD . /
RUN apt-get update
RUN apt-get -y install python-pip
RUN pip install awscli --ignore-installed six
RUN mvn clean package
CMD aws s3 cp s3://cnu-2016/abansal/application.properties target/application.properties && java -jar target/SpringProject-1.0-SNAPSHOT.jar --spring.config.location=./target/application.properties
