# spring-cloud-gateway-sample

# gateway-demo

Sample project to demonstrate Spring Cloud Gateway.

See the different application properties files for each example.

To re-create base project:
```
name=gateway-demo
dep=cloud-gateway,actuator,cloud-resilience4j,data-redis-reactive
http https://start.spring.io/starter.zip dependencies==$dep name==$name artifactId==$name baseDir==$name | tar -xzvf -
```
