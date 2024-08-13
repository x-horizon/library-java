// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.contract.aspect;

import cn.srd.library.java.contract.model.protocol.MessageModel;
import cn.srd.library.java.message.engine.contract.model.enums.MessageEngineType;
import cn.srd.library.java.tool.spring.contract.support.AopCaptor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.messaging.support.GenericMessage;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author wjm
 * @since 2024-05-27 14:37
 */
public abstract class MessageAspect implements AopCaptor {

    public boolean sendMessage(ProceedingJoinPoint joinPoint, MessageEngineType messageEngineType, Serializable message) {
        Method producerMethod = getMethod(joinPoint);
        return messageEngineType
                .getConfigStrategy()
                .getIntegrationFlowRegistration(
                        producerMethod,
                        getMethodParameterNames(joinPoint),
                        joinPoint.getArgs(),
                        messageEngineType.getFlowStrategy().getFlowId(producerMethod)
                )
                .getInputChannel()
                .send(new GenericMessage<>(message instanceof MessageModel ? message : MessageModel.builder().data(message).build()));
    }

}