FROM openjdk:17-oracle
WORKDIR /app
COPY target/demo-0.0.1-SNAPSHOT-spring-boot.jar app.jar
#Введите в свойство param значение admin или guest
ENV param: admin
CMD ["java", "-jar", "app.jar"]