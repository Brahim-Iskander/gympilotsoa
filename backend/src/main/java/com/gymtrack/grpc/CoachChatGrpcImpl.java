package com.gymtrack.grpc;

import java.util.List;

import com.gymtrack.dto.coach.CoachMessageResponse;
import com.gymtrack.grpc.chat.*;
import com.gymtrack.service.CoachChatService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class CoachChatGrpcImpl extends CoachChatGrpcServiceGrpc.CoachChatGrpcServiceImplBase {

    private final CoachChatService coachChatService;

    public CoachChatGrpcImpl(CoachChatService coachChatService) {
        this.coachChatService = coachChatService;
    }

    @Override
    public StreamObserver<ChatMessageRequest> streamMessages(StreamObserver<ChatMessageResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(ChatMessageRequest request) {
                CoachMessageResponse saved = coachChatService.sendMessageFromUser(
                        request.getUserEmail(), request.getMessage());

                ChatMessageResponse grpcResponse = ChatMessageResponse.newBuilder()
                        .setId(saved.getId())
                        .setUserId(saved.getUserId() != null ? saved.getUserId() : "")
                        .setUserFullName(saved.getUserFullName() != null ? saved.getUserFullName() : "")
                        .setUserEmail(saved.getUserEmail() != null ? saved.getUserEmail() : "")
                        .setSenderRole(saved.getSenderRole() != null ? saved.getSenderRole() : "")
                        .setSenderName(saved.getSenderName() != null ? saved.getSenderName() : "")
                        .setMessage(saved.getMessage() != null ? saved.getMessage() : "")
                        .setReadByUser(saved.isReadByUser())
                        .setReadByCoach(saved.isReadByCoach())
                        .setCreatedAtMillis(saved.getCreatedAt() != null ? saved.getCreatedAt().toEpochMilli() : 0)
                        .build();

                responseObserver.onNext(grpcResponse);
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getMessages(GetMessagesRequest request, StreamObserver<GetMessagesResponse> responseObserver) {
        List<CoachMessageResponse> messages = coachChatService.getMessagesForUser(request.getUserEmail());

        GetMessagesResponse.Builder responseBuilder = GetMessagesResponse.newBuilder();
        for (CoachMessageResponse msg : messages) {
            responseBuilder.addMessages(toGrpcMessage(msg));
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUnreadCount(GetUnreadCountRequest request, StreamObserver<UnreadCountResponse> responseObserver) {
        long count = coachChatService.getUnreadCountForUser(request.getUserEmail());
        responseObserver.onNext(UnreadCountResponse.newBuilder().setUnreadCount(count).build());
        responseObserver.onCompleted();
    }

    private ChatMessageResponse toGrpcMessage(CoachMessageResponse msg) {
        return ChatMessageResponse.newBuilder()
                .setId(msg.getId() != null ? msg.getId() : "")
                .setUserId(msg.getUserId() != null ? msg.getUserId() : "")
                .setUserFullName(msg.getUserFullName() != null ? msg.getUserFullName() : "")
                .setUserEmail(msg.getUserEmail() != null ? msg.getUserEmail() : "")
                .setSenderRole(msg.getSenderRole() != null ? msg.getSenderRole() : "")
                .setSenderName(msg.getSenderName() != null ? msg.getSenderName() : "")
                .setMessage(msg.getMessage() != null ? msg.getMessage() : "")
                .setReadByUser(msg.isReadByUser())
                .setReadByCoach(msg.isReadByCoach())
                .setCreatedAtMillis(msg.getCreatedAt() != null ? msg.getCreatedAt().toEpochMilli() : 0)
                .build();
    }
}
