package org.horizon.sdk.library.java.message.engine.client.contract.strategy;

import org.horizon.sdk.library.java.tool.id.uuid.support.UUIDs;

/**
 * @author wjm
 * @since 2024-05-30 10:54
 */
public class MessageClientUniqueIdGenerateByUUIDStrategy implements MessageClientUniqueIdGenerateStrategy<String> {

    @Override
    public String getId() {
        return UUIDs.fastGetString();
    }

}