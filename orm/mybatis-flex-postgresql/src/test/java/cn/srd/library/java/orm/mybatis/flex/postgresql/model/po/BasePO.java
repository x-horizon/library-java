package cn.srd.library.java.orm.mybatis.flex.postgresql.model.po;

import cn.srd.library.java.orm.contract.model.base.PO;
import cn.srd.library.java.tool.convert.jackson.deserializer.JacksonLongToLocalDateTimeDeserializer;
import cn.srd.library.java.tool.convert.jackson.serializer.JacksonLocalDateTimeToLongSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mybatisflex.annotation.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
public class BasePO implements PO {

    @Serial private static final long serialVersionUID = 2152316212528302390L;

    @Column(value = "version", version = true)
    private Long version;

    @Column(value = "creator_id")
    private Long creatorId;

    @Column(value = "create_time")
    @JsonSerialize(using = JacksonLocalDateTimeToLongSerializer.class)
    @JsonDeserialize(converter = JacksonLongToLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Column(value = "row_is_deleted", isLogicDelete = true)
    private Boolean rowIsDeleted;

}
