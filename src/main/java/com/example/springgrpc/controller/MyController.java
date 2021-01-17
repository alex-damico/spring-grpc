package com.example.springgrpc.controller;

import com.example.springgrpc.service.GrpcMyClient;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("client")
public class MyController {

  private final GrpcMyClient grpcClient;

  public MyController(GrpcMyClient grpcClient) {
    this.grpcClient = grpcClient;
  }

  @GetMapping("/")
  public ResponseEntity<String> request() {
    this.grpcClient.sendRequest();
    return ResponseEntity.of(Optional.of("OK"));
  }

}
