FROM openjdk:17-oracle
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT-spring-boot.jar app.jar
#Введите в свойство param1 значение admin или guest
ENV param1: admin
ENV param2: /app/students.json
CMD ["java", "-jar", "app.jar"]