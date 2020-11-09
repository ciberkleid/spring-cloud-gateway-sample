# spring-cloud-gateway-sample

# gateway-demo

Sample project to demonstrate Spring Cloud Gateway.

See the different application properties files for each example.

For each demo, run the following command and follow the instructions in the corresponding application-demoX.yml file:
```
cd gateway-demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo1
```

For the Rate Limiting example, you will need an instance of redis:
```
docker run -d --name redis -p 6379:6379 redis
```

To re-create base project:
```
name=gateway-demo
dep=cloud-gateway,actuator,cloud-resilience4j,data-redis-reactive
http https://start.spring.io/starter.zip dependencies==$dep name==$name artifactId==$name baseDir==$name | tar -xzvf -
```
