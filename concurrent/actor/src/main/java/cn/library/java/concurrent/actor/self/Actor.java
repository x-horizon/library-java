package cn.library.java.concurrent.actor.self;

import cn.library.java.concurrent.actor.message.ActorMessage;
import cn.library.java.concurrent.actor.reference.ActorContext;
import cn.library.java.concurrent.actor.reference.ActorReference;

/**
 * @author wjm
 * @since 2025-01-26 21:06
 */
public interface Actor {

    default void init(ActorContext context) {
    }

    default void onInitFailure(int attemptCount, Throwable cause) {
    }

    default void destroy(Throwable cause) {
    }

    void process(ActorMessage message);

    default void onProcessFailure(ActorMessage message, Throwable cause) {
    }

    ActorReference getReference();

}