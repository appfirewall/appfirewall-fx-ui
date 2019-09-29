package io.appfirewall.fx.ui.communication;

import com.google.common.flogger.FluentLogger;
import io.appfirewall.protocol.*;
import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import io.grpc.stub.StreamObserver;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import javafx.application.Platform;
import io.appfirewall.fx.ui.prompt.Prompt;

import java.io.IOException;
import java.util.concurrent.FutureTask;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

public class UnixSocketGrpcServer {
    private static final FluentLogger logger = FluentLogger.forEnclosingClass();

    private Server server;

    public void start(String unixSocketPath) throws IOException {

        EventLoopGroup elg = new EpollEventLoopGroup();

        EventLoopGroup boss = new EpollEventLoopGroup(1);
        server = NettyServerBuilder
                .forAddress(new DomainSocketAddress(unixSocketPath))
                .bossEventLoopGroup(boss)
                .addService(new UIGrpcImpl())
                .workerEventLoopGroup(elg)
                .channelType(EpollServerDomainSocketChannel.class)
                .build()
                .start();

        logger.atInfo().log("Server started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            UnixSocketGrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));

    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    static class UIGrpcImpl extends AFIGrpc.AFIImplBase {

        @Override
        public void sendStats(AFStats request, StreamObserver<AFStatsReply> responseObserver) {
            //super.ping(request, responseObserver);
            //logger.atInfo().log("ping");
            // TODO do something on Ping
            var reply = AFStatsReply.newBuilder()
                    .setId(request.getId())
                    .build();
            StatisticsHolder.setStatistics(request.getStats());
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }

        public void prompt(AFConnectionInfo request, StreamObserver<AFRule> responseObserver) {
            //super.askRule(request, responseObserver);

            logger.atInfo().log("askRule " + request.getDstHost());
            final var promptTask = new FutureTask<>(() ->
            {
                var prompt = new Prompt(request);
                return prompt.open();
            }
            );

            Platform.runLater(promptTask);

            try {
                promptTask.get().ifPresentOrElse(
                        (rule) -> {
                            responseObserver.onNext(rule);
                            logger.atInfo().log(rule.toString());
                            responseObserver.onCompleted();
                        },
                        () -> logger.atSevere().log("No Rule received")
                );
            } catch (Exception e) {
                logger.atSevere().withCause(e).log();
            }
        }
    }
}
