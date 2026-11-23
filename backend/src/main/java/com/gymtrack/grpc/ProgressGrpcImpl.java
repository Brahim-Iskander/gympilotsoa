package com.gymtrack.grpc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gymtrack.dto.progress.ProgressEntryRequest;
import com.gymtrack.dto.progress.ProgressEntryResponse;
import com.gymtrack.grpc.progress.*;
import com.gymtrack.service.ProgressService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProgressGrpcImpl extends ProgressGrpcServiceGrpc.ProgressGrpcServiceImplBase {

    private final ProgressService progressService;

    public ProgressGrpcImpl(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Override
    public void createEntry(CreateProgressEntryRequest request,
                            StreamObserver<com.gymtrack.grpc.progress.ProgressEntryResponse> responseObserver) {
        try {
            List<com.gymtrack.model.ProgressEntry.StrengthLog> strengthLogs = new ArrayList<>();
            for (StrengthLog grpcLog : request.getStrengthLogsList()) {
                com.gymtrack.model.ProgressEntry.StrengthLog log = new com.gymtrack.model.ProgressEntry.StrengthLog();
                log.setExerciseName(grpcLog.getExercise());
                log.setWeight(grpcLog.getWeight());
                log.setReps(grpcLog.getReps());
                log.setSets(grpcLog.getSets());
                strengthLogs.add(log);
            }

            ProgressEntryRequest entryRequest = new ProgressEntryRequest(
                    LocalDate.parse(request.getDate()),
                    request.getWeight(),
                    request.getWeightUnit(),
                    request.getMeasurementsMap(),
                    request.getMeasurementUnit(),
                    strengthLogs,
                    null,
                    request.getNote()
            );

            ProgressEntryResponse saved = progressService.createEntry(request.getUserEmail(), entryRequest);
            responseObserver.onNext(toGrpcEntry(saved));
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("Error creating progress entry: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    @Override
    public void getEntries(GetEntriesRequest request,
                           StreamObserver<GetEntriesResponse> responseObserver) {
        List<ProgressEntryResponse> entries = progressService.getEntriesForUser(request.getUserEmail());

        GetEntriesResponse.Builder builder = GetEntriesResponse.newBuilder();
        for (ProgressEntryResponse entry : entries) {
            builder.addEntries(toGrpcEntry(entry));
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteEntry(DeleteEntryRequest request,
                            StreamObserver<DeleteEntryResponse> responseObserver) {
        try {
            progressService.deleteEntry(request.getUserEmail(), request.getEntryId());
            responseObserver.onNext(DeleteEntryResponse.newBuilder().setSuccess(true).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND
                            .withDescription("Entry not found: " + e.getMessage())
                            .asRuntimeException());
        }
    }

    private com.gymtrack.grpc.progress.ProgressEntryResponse toGrpcEntry(ProgressEntryResponse entry) {
        com.gymtrack.grpc.progress.ProgressEntryResponse.Builder builder =
                com.gymtrack.grpc.progress.ProgressEntryResponse.newBuilder()
                        .setId(entry.id() != null ? entry.id() : "")
                        .setUserId(entry.userId() != null ? entry.userId() : "")
                        .setDate(entry.date() != null ? entry.date().toString() : "")
                        .setWeight(entry.weight() != null ? entry.weight() : 0)
                        .setWeightUnit(entry.weightUnit() != null ? entry.weightUnit() : "kg")
                        .setMeasurementUnit(entry.measurementUnit() != null ? entry.measurementUnit() : "cm")
                        .setNote(entry.note() != null ? entry.note() : "")
                        .setCreatedAtMillis(entry.createdAt() != null ? entry.createdAt().toEpochMilli() : 0)
                        .setUpdatedAtMillis(entry.updatedAt() != null ? entry.updatedAt().toEpochMilli() : 0);

        if (entry.measurements() != null) {
            builder.putAllMeasurements(entry.measurements());
        }

        if (entry.strengthLogs() != null) {
            for (var log : entry.strengthLogs()) {
                builder.addStrengthLogs(StrengthLog.newBuilder()
                        .setExercise(log.getExerciseName() != null ? log.getExerciseName() : "")
                        .setWeight(log.getWeight())
                        .setReps(log.getReps())
                        .setSets(log.getSets())
                        .build());
            }
        }

        return builder.build();
    }
}
