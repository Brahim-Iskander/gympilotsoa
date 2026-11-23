package com.gymtrack.grpc;

import com.gymtrack.grpc.ai.*;
import com.gymtrack.service.AiService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class AiGrpcImpl extends AiGrpcServiceGrpc.AiGrpcServiceImplBase {

    private final AiService aiService;

    public AiGrpcImpl(AiService aiService) {
        this.aiService = aiService;
    }

    @Override
    public void chatStream(AiChatRequest request, StreamObserver<AiChatChunk> responseObserver) {
        try {
            String fullResponse = aiService.chatWithAi(request.getMessage());

            int chunkSize = 200;
            for (int i = 0; i < fullResponse.length(); i += chunkSize) {
                String chunk = fullResponse.substring(i, Math.min(i + chunkSize, fullResponse.length()));
                boolean isFinal = (i + chunkSize) >= fullResponse.length();

                AiChatChunk grpcChunk = AiChatChunk.newBuilder()
                        .setChunk(chunk)
                        .setIsFinal(isFinal)
                        .build();
                responseObserver.onNext(grpcChunk);
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("AI service error: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    @Override
    public void getUsageStatus(UsageStatusRequest request, StreamObserver<UsageStatusResponse> responseObserver) {
        UsageStatusResponse response = UsageStatusResponse.newBuilder()
                .setPlan("FREE")
                .setDailyUsed(0)
                .setDailyLimit(5)
                .setMonthlyUsed(0)
                .setMonthlyLimit(50)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
