// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.message.engine.mqtt.v3.strategy;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.srd.library.java.message.engine.contract.MessageConsumer;
import cn.srd.library.java.message.engine.contract.MessageProducer;
import cn.srd.library.java.message.engine.contract.model.dto.MessageConfigDTO;
import cn.srd.library.java.message.engine.contract.model.dto.MessageVerificationConfigDTO;
import cn.srd.library.java.message.engine.contract.model.enums.ClientIdGenerateType;
import cn.srd.library.java.message.engine.contract.model.enums.MessageEngineType;
import cn.srd.library.java.message.engine.contract.strategy.MessageConfigStrategy;
import cn.srd.library.java.message.engine.contract.support.MessageFlows;
import cn.srd.library.java.message.engine.mqtt.v3.MessageMqttV3Config;
import cn.srd.library.java.message.engine.mqtt.v3.model.dto.MessageMqttV3ConfigDTO;
import cn.srd.library.java.message.engine.mqtt.v3.model.properties.MessageMqttV3Properties;
import cn.srd.library.java.tool.lang.convert.Converts;
import cn.srd.library.java.tool.lang.functional.Assert;
import cn.srd.library.java.tool.lang.time.Times;
import cn.srd.library.java.tool.spring.contract.Springs;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author wjm
 * @since 2024-06-04 17:10
 */
@Slf4j
public class MessageMqttV3ConfigStrategy extends MessageConfigStrategy<MessageMqttV3Properties, MessageMqttV3ConfigDTO, MessageMqttV3ConfigDTO.BrokerDTO, MessageMqttV3ConfigDTO.ClientDTO, MessageMqttV3ConfigDTO.ProducerDTO, MessageMqttV3ConfigDTO.ConsumerDTO> {

    @Autowired MessageMqttV3Properties mqttV3Properties;

    @Override
    protected MessageVerificationConfigDTO getVerificationConfigDTO(MessageMqttV3ConfigDTO configDTO) {
        MessageVerificationConfigDTO verificationConfigDTO = new MessageVerificationConfigDTO();
        return verificationConfigDTO;
    }

    @Override
    protected Class<MessageMqttV3ConfigDTO> getConfigType() {
        return MessageMqttV3ConfigDTO.class;
    }

    @Override
    protected Class<MessageMqttV3Properties> getPropertiesType() {
        return MessageMqttV3Properties.class;
    }

    @Override
    protected MessageMqttV3ConfigDTO.BrokerDTO getBrokerDTO() {
        return MessageMqttV3ConfigDTO.BrokerDTO.builder()
                .serverUrls(this.mqttV3Properties.getServerUrls())
                .username(this.mqttV3Properties.getUsername())
                .password(this.mqttV3Properties.getPassword())
                .build();
    }

    @Override
    protected MessageMqttV3ConfigDTO.ClientDTO getClientDTO(Annotation clientConfig, Method executeMethod) {
        MessageMqttV3Config.ClientConfig clientConfigAnnotation = (MessageMqttV3Config.ClientConfig) clientConfig;
        String flowId = MessageFlows.getFlowId(MessageEngineType.MQTT_V3, executeMethod);
        ClientIdGenerateType idGenerateType = clientConfigAnnotation.idGenerateType();
        return MessageMqttV3ConfigDTO.ClientDTO.builder()
                .clientId(MessageFlows.getDistributedUniqueClientId(idGenerateType, flowId))
                .flowId(flowId)
                .idGenerateType(idGenerateType)
                .executeMethod(executeMethod)
                .qosType(clientConfigAnnotation.qosType())
                .completionTimeout(clientConfigAnnotation.completionTimeout())
                .disconnectCompletionTimeout(clientConfigAnnotation.disconnectCompletionTimeout())
                .originalCompletionTimeout(Times.wrapper(clientConfigAnnotation.completionTimeout()).toMillisecond().toMillis())
                .originalDisconnectCompletionTimeout(Times.wrapper(clientConfigAnnotation.disconnectCompletionTimeout()).toMillisecond().toMillis())
                .build();
    }

    @Override
    protected MessageMqttV3ConfigDTO.ProducerDTO getProducerDTO(Method executeMethod, MessageProducer producerAnnotation) {
        MessageMqttV3Config.ProducerConfig producerConfig = producerAnnotation.config().mqttV3().producerConfig();
        return MessageMqttV3ConfigDTO.ProducerDTO.builder()
                .clientDTO(getClientDTO(producerAnnotation.config().mqttV3().clientConfig(), executeMethod))
                .topic(producerAnnotation.topic())
                .needToSendAsync(producerConfig.needToSendAsync())
                .build();
    }

    @Override
    protected MessageMqttV3ConfigDTO.ConsumerDTO getConsumerDTO(Method executeMethod, MessageConsumer consumerAnnotation, MessageConfigDTO.ProducerDTO forwardProducerDTO) {
        return MessageMqttV3ConfigDTO.ConsumerDTO.builder()
                .clientDTO(getClientDTO(consumerAnnotation.config().mqttV3().clientConfig(), executeMethod))
                .forwardProducerDTO(forwardProducerDTO)
                .topics(Converts.toList(consumerAnnotation.topics()))
                .build();
    }

    @Override
    protected IntegrationFlow getProducerFlow(MessageMqttV3ConfigDTO.ProducerDTO producerDTO) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(producerDTO.getClientDTO().getClientId(), Springs.getBean(MqttPahoClientFactory.class));
        messageHandler.setDefaultTopic(producerDTO.getTopic());
        messageHandler.setDefaultQos(producerDTO.getClientDTO().getQosType().getCode());
        messageHandler.setAsync(producerDTO.isNeedToSendAsync());
        messageHandler.setCompletionTimeout(producerDTO.getClientDTO().getOriginalCompletionTimeout());
        messageHandler.setDisconnectCompletionTimeout(producerDTO.getClientDTO().getOriginalDisconnectCompletionTimeout());
        return MessageFlows.getObjectToStringIntegrationFlow(messageHandler);
    }

    @Override
    protected IntegrationFlow getConsumerFlow(MessageMqttV3ConfigDTO.ConsumerDTO consumerDTO) {
        DefaultMqttPahoClientFactory mqttClientFactory = Springs.getBean(DefaultMqttPahoClientFactory.class);
        MqttPahoMessageDrivenChannelAdapter messageDrivenChannelAdapter = new MqttPahoMessageDrivenChannelAdapter(consumerDTO.getClientDTO().getClientId(), mqttClientFactory, Converts.toArray(consumerDTO.getTopics(), String.class));
        messageDrivenChannelAdapter.setQos(consumerDTO.getClientDTO().getQosType().getCode());
        messageDrivenChannelAdapter.setConverter(new DefaultPahoMessageConverter());
        messageDrivenChannelAdapter.setCompletionTimeout(consumerDTO.getClientDTO().getOriginalCompletionTimeout());
        messageDrivenChannelAdapter.setDisconnectCompletionTimeout(consumerDTO.getClientDTO().getOriginalDisconnectCompletionTimeout());

        Object consumerInstance = Springs.getBean(consumerDTO.getClientDTO().getExecuteMethod().getDeclaringClass());
        Assert.of().setMessage("{}could not find the consumer instance in spring ioc, the class info is: [{}], please add it into spring ioc!", ModuleView.MESSAGE_ENGINE_SYSTEM, consumerDTO.getClientDTO().getExecuteMethod().getDeclaringClass().getName())
                .setThrowable(LibraryJavaInternalException.class)
                .throwsIfNull(consumerInstance);
        return IntegrationFlow.from(messageDrivenChannelAdapter)
                .handle(MessageFlows.getStringToObjectMessageHandler(consumerInstance, consumerDTO.getClientDTO().getExecuteMethod()))
                .get();
    }

    @Override
    protected void registerClientFactory(MessageMqttV3ConfigDTO.BrokerDTO brokerDTO) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(Converts.toArray(brokerDTO.getServerUrls(), String.class));
        Optional.ofNullable(brokerDTO.getUsername()).ifPresent(mqttConnectOptions::setUserName);
        Optional.ofNullable(brokerDTO.getPassword()).ifPresent(value -> mqttConnectOptions.setPassword(value.toCharArray()));
        DefaultMqttPahoClientFactory mqttClientFactory = new DefaultMqttPahoClientFactory();
        mqttClientFactory.setConnectionOptions(mqttConnectOptions);
        Springs.registerBean(DefaultMqttPahoClientFactory.class.getName(), mqttClientFactory);
    }

    @Override
    protected void registerProducerFactory(MessageMqttV3ConfigDTO.ProducerDTO producerDTO) {

    }

    @Override
    protected void registerConsumerFactory(MessageMqttV3ConfigDTO.ConsumerDTO consumerDTO) {

    }

}