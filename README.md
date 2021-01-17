# spring-grpc

Server:
- 

Start server:
```
java -jar -Dspring.profiles.active=server target/spring-grpc-0.0.1-SNAPSHOT.jar
```

Start client:
```
java -jar -Dspring.profiles.active=client target/spring-grpc-0.0.1-SNAPSHOT.jar
```

Request gRPC:
```
curl http://localhost:6012/
```