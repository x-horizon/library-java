// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.contract.support;

import cn.srd.library.java.contract.constant.suppress.SuppressWarningConstant;
import cn.srd.library.java.contract.model.protocol.MessageModel;
import cn.srd.library.java.message.engine.contract.model.enums.ClientIdGenerateType;
import cn.srd.library.java.message.engine.contract.model.enums.MessageEngineType;
import cn.srd.library.java.tool.convert.api.Converts;
import cn.srd.library.java.tool.lang.collection.Collections;
import cn.srd.library.java.tool.lang.object.Methods;
import cn.srd.library.java.tool.lang.reflect.Reflects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageHandlerSpec;
import org.springframework.messaging.MessageHandler;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author wjm
 * @since 2024-05-27 18:11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageFlows {

    private static final Map<Method, Map<MessageEngineType, String>> STATIC_FLOW_ID_CACHE = Collections.newConcurrentHashMap(256);

    @SuppressWarnings(SuppressWarningConstant.PREVIEW)
    public static String getStaticFlowId(MessageEngineType engineType, Method annotatedMethod) {
        return STATIC_FLOW_ID_CACHE.computeIfAbsent(annotatedMethod, _ -> Collections.newConcurrentHashMap()).computeIfAbsent(engineType, _ -> STR."\{engineType.getDescription()}-\{Methods.getFullName(annotatedMethod)}");
    }

    @SuppressWarnings(SuppressWarningConstant.PREVIEW)
    public static String getDynamicFlowId(MessageEngineType engineType, Method annotatedMethod, String topic) {
        return STR."\{getStaticFlowId(engineType, annotatedMethod)}-\{topic}";
    }

    @SuppressWarnings(SuppressWarningConstant.PREVIEW)
    public static String getDistributedUniqueClientId(ClientIdGenerateType generateType, String flowId) {
        return STR."\{flowId}-\{generateType.getStrategy().getId()}";
    }

    public static MessageHandler getStringToObjectMessageHandler(Object consumerInstance, Method consumerMethod) {
        return message -> Reflects.invoke(consumerInstance, consumerMethod, Converts.onJackson().toBean((String) message.getPayload(), MessageModel.class).requireSuccessAndGetData());
    }

    public static IntegrationFlow getObjectToStringIntegrationFlow(MessageHandler messageHandler) {
        return flow -> flow.transform(getObjectToStringTransformer()).handle(messageHandler);
    }

    public static <H extends MessageHandler> IntegrationFlow getObjectToStringIntegrationFlow(MessageHandlerSpec<?, H> messageHandlerSpec) {
        return flow -> flow.transform(getObjectToStringTransformer()).handle(messageHandlerSpec);
    }

    public static GenericTransformer<?, ?> getObjectToStringTransformer() {
        return messageData -> Converts.onJackson().toString(messageData);
    }

}