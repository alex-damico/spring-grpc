package com.example.springgrpc.service;

import com.example.springgrpc.HelloRequest;
import com.example.springgrpc.HelloResponse;
import com.example.springgrpc.MyServiceGrpc;
import com.example.springgrpc.MyServiceGrpc.MyServiceBlockingStub;
import com.example.springgrpc.MyServiceGrpc.MyServiceStub;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("client")
public class GrpcMyClient {

  @GrpcClient("my-service")
  private MyServiceBlockingStub myServiceBlockingStub;

  private StreamObserver<HelloResponse> getResponseObserver() {
    return new StreamObserver<HelloResponse>() {

      @Override
      public void onNext(HelloResponse helloResponse) {
        log.info("helloResponse: " + helloResponse.getText());
      }

      @Override
      public void onError(Throwable throwable) {
        log.error("on error", throwable);
//        throw new RuntimeException(throwable);
      }

      @Override
      public void onCompleted() {
        log.info("on completed");
      }
    };
  }

  public void sendRequest() {
    log.info("Start - " + LocalDateTime.now().toString());

    MyServiceStub myServiceStub = MyServiceGrpc.newStub(myServiceBlockingStub.getChannel());
    StreamObserver<HelloRequest> streamObserver = myServiceStub.subscribe(getResponseObserver());
    HelloRequest helloRequest = HelloRequest.newBuilder().setText("request").build();
    try {
      streamObserver.onNext(helloRequest);
      streamObserver.onCompleted();
    } catch (StatusRuntimeException ex) {
      log.info("Error", ex);
    }
    log.info("Stop - " + LocalDateTime.now().toString());
  }
}
