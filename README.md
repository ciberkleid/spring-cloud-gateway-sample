# spring-cloud-gateway-sample

# gateway-demo

Sample project to demonstrate Spring Cloud Gateway.

Each demo is captured in a separate application properties file, starting with [application-demo1.yml](gateway-demo/src/main/resources/application-demo1.yml).

For each demo, review the instructions in the corresponding properties file.

For each demo, you need to stop the gateway application and then restart it using the following command:
```
cd gateway-demo
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo1
```

Note that for the Rate Limiting example, you will need an instance of redis:
```
docker run -d --rm --name redis -p 6379:6379 redis
```
