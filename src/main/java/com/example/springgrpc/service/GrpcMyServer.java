package com.example.springgrpc.service;

import com.example.springgrpc.HelloRequest;
import com.example.springgrpc.HelloResponse;
import com.example.springgrpc.MyServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Profile;

@Slf4j
@GrpcService
@Profile("server")
public class GrpcMyServer extends MyServiceGrpc.MyServiceImplBase {

  public StreamObserver<HelloRequest> subscribe(
      StreamObserver<HelloResponse> responseStreamObserver) {
    return new StreamObserver<HelloRequest>() {
      @Override
      public void onNext(HelloRequest helloRequest) {
        log.info("helloRequest: " + helloRequest.getText());

        HelloResponse helloResponse = HelloResponse.newBuilder().setText("welcome").build();
        responseStreamObserver.onNext(helloResponse);
      }

      @Override
      public void onError(Throwable throwable) {
        log.info("on error", throwable);
//        throw new RuntimeException(throwable);
      }

      @Override
      public void onCompleted() {
        log.info("on completed");
      }
    };
  }

}
