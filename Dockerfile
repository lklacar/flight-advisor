FROM maven:3.6.3-jdk-11

EXPOSE 8080
COPY . /app/
WORKDIR /app
RUN mvn clean package -DskipTests -Pdocker
ENTRYPOINT exec java -jar target/flight-advisor-0.0.1-SNAPSHOT.jar
