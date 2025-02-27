package org.horizon.sdk.library.java.orm.mybatis.flex.base.audit;

import com.mybatisflex.core.audit.AuditMessage;
import com.mybatisflex.core.audit.MessageReporter;

import java.util.List;

/**
 * the {@link AuditMessage} telemeter TODO wjm implement me
 *
 * @author wjm
 * @since 2023-11-14 14:53
 */
public sealed interface AuditLogTelemeter extends MessageReporter permits UnsupportedAuditLogTelemeter {

    @Override
    default void sendMessages(List<AuditMessage> auditLogs) {
        send(auditLogs);
    }

    /**
     * the audit log telemetry action
     *
     * @param auditLogs the audit log
     */
    void send(List<AuditMessage> auditLogs);

}