// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.web.grpc.server.interceptor;

import cn.srd.library.java.contract.model.throwable.ClientException;
import cn.srd.library.java.contract.model.throwable.DataNotFoundException;
import cn.srd.library.java.contract.model.throwable.RunningException;
import cn.srd.library.java.contract.model.throwable.UnsupportedException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

/**
 * @author wjm
 * @since 2024-06-13 23:24
 */
@Slf4j
@GrpcAdvice
public class GrpcServerExceptionInterceptor {

    @GrpcExceptionHandler(UnsupportedException.class)
    public Status handleUnsupportedException(UnsupportedException exception) {
        return Status.UNIMPLEMENTED.withDescription(exception.getMessage()).withCause(exception);
    }

    @GrpcExceptionHandler(DataNotFoundException.class)
    public Status handleDataNotFoundException(DataNotFoundException exception) {
        return Status.NOT_FOUND.withDescription(exception.getMessage()).withCause(exception);
    }

    @GrpcExceptionHandler(ClientException.class)
    public Status handleClientException(ClientException exception) {
        return Status.ABORTED.withDescription(exception.getMessage()).withCause(exception);
    }

    @GrpcExceptionHandler(RunningException.class)
    public Status handleRunningException(RunningException exception) {
        return Status.ABORTED.withDescription(exception.getMessage()).withCause(exception);
    }

    @GrpcExceptionHandler(Throwable.class)
    public Status handleThrowable(Throwable exception) {
        return Status.INTERNAL.withDescription(exception.getMessage()).withCause(exception);
    }

}