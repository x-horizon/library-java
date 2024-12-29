package cn.srd.library.java.studio.low.code.model.bo;

import cn.srd.library.java.doc.knife4j.contract.constant.ApiDocConstant;
import cn.srd.library.java.orm.contract.mybatis.flex.model.bo.BaseWithVersionBO;
import cn.srd.library.java.orm.contract.mybatis.postgresql.handler.JdbcJsonbMappingJavaListEnumIntegerTypeHandler;
import cn.srd.library.java.studio.low.code.model.enums.TeacherLevelType;
import cn.srd.library.java.studio.low.code.model.enums.TeacherStatus;
import cn.srd.library.java.studio.low.code.model.po.TeacherPO;
import cn.srd.library.java.studio.low.code.model.vo.TeacherVO;
import cn.srd.library.java.tool.convert.jackson.deserializer.JacksonEnumValueToEnumDeserializer;
import cn.srd.library.java.tool.convert.jackson.deserializer.JacksonListEnumValueToListEnumDeserializer;
import cn.srd.library.java.tool.convert.jackson.serializer.JacksonEnumToIntegerSerializer;
import cn.srd.library.java.tool.convert.jackson.serializer.JacksonListEnumToListIntegerSerializer;
import cn.srd.library.java.tool.convert.jackson.serializer.JacksonLongToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.util.List;

/**
 * 教师信息 model
 *
 * @author TODO 请填写作者名字
 * @since 2024-04-15 23:57
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@AutoMappers({@AutoMapper(target = TeacherPO.class), @AutoMapper(target = TeacherVO.class)})
public class TeacherBO extends BaseWithVersionBO {

    @Serial private static final long serialVersionUID = -8552109224294597412L;

    @Schema(description = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.NUMBER)
    @Column(value = "id")
    @Id
    @JsonSerialize(using = JacksonLongToStringSerializer.class)
    private Long id;

    @Schema(description = "学校id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.NUMBER)
    @Column(value = "school_id")
    @JsonSerialize(using = JacksonLongToStringSerializer.class)
    private Long schoolId;

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.STRING)
    @Column(value = "code")
    private Long code;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.STRING)
    @Column(value = "name")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.NUMBER)
    @Column(value = "status")
    @JsonSerialize(using = JacksonEnumToIntegerSerializer.class)
    @JsonDeserialize(using = JacksonEnumValueToEnumDeserializer.class)
    private TeacherStatus status;

    @Schema(description = "等级类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.LIST_NUMBER)
    @Column(value = "level_types", typeHandler = JdbcJsonbMappingJavaListEnumIntegerTypeHandler.class)
    @JsonDeserialize(using = JacksonListEnumValueToListEnumDeserializer.class)
    @JsonSerialize(using = JacksonListEnumToListIntegerSerializer.class)
    private List<TeacherLevelType> levelTypes;

    // @Schema(description = "课程类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = ApiDocConstant.LIST_STRING)
    // @Column(value = "course_types", typeHandler = JdbcJsonbMappingJavaListEnumStringTypeHandler.class)
    // @JsonDeserialize(using = JacksonListEnumValueToListEnumDeserializer.class)
    // @JsonSerialize(using = JacksonListEnumToListStringSerializer.class)
    // private List<TeacherCourseType> courseTypes;

}