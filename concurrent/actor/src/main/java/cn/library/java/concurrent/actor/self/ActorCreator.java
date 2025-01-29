package cn.library.java.concurrent.actor.self;

import cn.library.java.concurrent.actor.id.ActorId;

/**
 * @author wjm
 * @since 2025-01-26 22:50
 */
public interface ActorCreator {

    ActorId createActorId();

    Actor createActor();

}