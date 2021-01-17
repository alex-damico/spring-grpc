package com.example.springgrpc.service;

import com.example.springgrpc.HelloRequest;
import com.example.springgrpc.HelloResponse;
import com.example.springgrpc.MyServiceGrpc;
import com.example.springgrpc.MyServiceGrpc.MyServiceBlockingStub;
import com.example.springgrpc.MyServiceGrpc.MyServiceStub;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import java.time.LocalDateTime;
import javax.annotation.PostConstruct;
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

  private MyServiceStub myServiceStub = null;
  private StreamObserver<HelloRequest> streamObserver = null;

  @PostConstruct
  private void init() {
    this.myServiceStub = MyServiceGrpc.newStub(myServiceBlockingStub.getChannel());
    this.streamObserver = myServiceStub.subscribe(getResponseObserver());
  }

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
    HelloRequest helloRequest = HelloRequest.newBuilder().setText("request").build();
    try {
      this.streamObserver.onNext(helloRequest);
      this.streamObserver.onCompleted();
    } catch (StatusRuntimeException ex) {
      log.info("Error", ex);
    }

    log.info("Stop - " + LocalDateTime.now().toString());
  }
}
