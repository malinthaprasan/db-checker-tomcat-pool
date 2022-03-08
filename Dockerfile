
# set base Docker image to AdoptOpenJDK Ubuntu Docker image
FROM adoptopenjdk:11.0.10_9-jdk-hotspot-focal
COPY target/db-checker-tomcat-pool-1.0-SNAPSHOT.jar /home
CMD cd /home && java -jar db-checker-tomcat-pool-1.0-SNAPSHOT.jar
