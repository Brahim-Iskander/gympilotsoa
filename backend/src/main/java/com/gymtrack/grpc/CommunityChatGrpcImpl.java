package com.gymtrack.grpc;

import java.util.List;

import com.gymtrack.dto.community.CommunityMessageResponse;
import com.gymtrack.grpc.community.*;
import com.gymtrack.service.CommunityChatService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CommunityChatGrpcImpl extends CommunityChatGrpcServiceGrpc.CommunityChatGrpcServiceImplBase {

    private final CommunityChatService communityChatService;

    public CommunityChatGrpcImpl(CommunityChatService communityChatService) {
        this.communityChatService = communityChatService;
    }

    @Override
    public void sendMessage(SendCommunityMessageRequest request,
                            StreamObserver<com.gymtrack.grpc.community.CommunityMessageResponse> responseObserver) {
        CommunityMessageResponse saved = communityChatService.sendMessage(
                request.getUserEmail(), request.getMessage());

        responseObserver.onNext(toGrpcMessage(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void getRecentMessages(GetRecentMessagesRequest request,
                                  StreamObserver<GetRecentMessagesResponse> responseObserver) {
        List<CommunityMessageResponse> messages = communityChatService.getRecentMessages();

        GetRecentMessagesResponse.Builder builder = GetRecentMessagesResponse.newBuilder();
        for (CommunityMessageResponse msg : messages) {
            builder.addMessages(toGrpcMessage(msg));
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeMessages(SubscribeRequest request,
                                  StreamObserver<com.gymtrack.grpc.community.CommunityMessageResponse> responseObserver) {
        List<CommunityMessageResponse> messages = communityChatService.getRecentMessages();
        for (CommunityMessageResponse msg : messages) {
            responseObserver.onNext(toGrpcMessage(msg));
        }
        responseObserver.onCompleted();
    }

    private com.gymtrack.grpc.community.CommunityMessageResponse toGrpcMessage(CommunityMessageResponse msg) {
        return com.gymtrack.grpc.community.CommunityMessageResponse.newBuilder()
                .setId(msg.getId() != null ? msg.getId() : "")
                .setUserId(msg.getUserId() != null ? msg.getUserId() : "")
                .setUserFullName(msg.getUserFullName() != null ? msg.getUserFullName() : "")
                .setUserEmail(msg.getUserEmail() != null ? msg.getUserEmail() : "")
                .setUserAvatar(msg.getUserAvatar() != null ? msg.getUserAvatar() : "")
                .setUserRole(msg.getUserRole() != null ? msg.getUserRole() : "")
                .setMessage(msg.getMessage() != null ? msg.getMessage() : "")
                .setCreatedAtMillis(msg.getCreatedAt() != null ? msg.getCreatedAt().toEpochMilli() : 0)
                .build();
    }
}
