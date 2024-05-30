package cn.srd.library.java.message.engine.contract.aspect;

import cn.srd.library.java.message.engine.contract.MessageProducer;
import cn.srd.library.java.message.engine.contract.support.MessageFlows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author wjm
 * @since 2023-05-25 17:02
 */
@Aspect
public class MessageProducerAspect extends MessageAspect {

    @Pointcut("@annotation(cn.srd.library.java.message.engine.contract.MessageProducer)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object aroundPointcut(ProceedingJoinPoint joinPoint) {
        Object message = doProceed(joinPoint);
        MessageProducer messageProducerAnnotation = getAnnotationMarkedOnMethod(joinPoint, MessageProducer.class);
        String flowId = MessageFlows.getUniqueFlowId(messageProducerAnnotation.engineConfig().type(), getMethod(joinPoint));
        messageProducerAnnotation.engineConfig()
                .type()
                .getStrategy()
                .registerProducerFlowIfNeed(flowId, messageProducerAnnotation)
                .send(flowId, message);
        return message;
    }

}