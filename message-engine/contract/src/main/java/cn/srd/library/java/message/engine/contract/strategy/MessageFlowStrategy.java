// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.contract.strategy;

import cn.srd.library.java.contract.model.protocol.MessageModel;
import cn.srd.library.java.tool.spring.contract.support.Springs;
import org.springframework.integration.dsl.context.IntegrationFlowContext;
import org.springframework.messaging.support.GenericMessage;

import java.lang.reflect.Method;

/**
 * @author wjm
 * @since 2024-05-27 11:53
 */
public interface MessageFlowStrategy {

    String getFlowId(Method producerMethod);

    default <T> boolean send(Method executeMethod, T message) {
        return send(getFlowId(executeMethod), message);
    }

    default <T> boolean send(String flowId, T message) {
        return Springs.getBean(IntegrationFlowContext.class)
                .getRegistrationById(flowId)
                .getInputChannel()
                .send(new GenericMessage<>(message instanceof MessageModel ? message : MessageModel.builder().data(message).build()));
    }

}