package cn.srd.itcp.sugar.mybatis.plus.support;

import cn.srd.itcp.sugar.tools.core.Objects;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * mybatis-plus 自动填充字段值，配合 {@link com.baomidou.mybatisplus.annotation.TableField} 使用
 * <pre>
 *    以下示例代码表示插入、更新、插入和更新时自动填充值：
 *
 *         &#064;TableName(value = "table_name", autoResultMap = true)
 *         public class TableNamePO implements Serializable {
 *             private static final long serialVersionUID = -4579156082783453314L;
 *             &#064;TableLogic
 *             &#064;TableField(fill = FieldFill.INSERT)
 *             private Boolean deleteFlag;
 *             &#064;TableField(fill = FieldFill.INSERT)
 *             private LocalDateTime createTime;
 *             &#064;TableField(fill = FieldFill.INSERT_UPDATE)
 *             private LocalDateTime updateTime;
 *         }
 * </pre>
 *
 * @author wjm
 * @date 2022-07-05
 */
public interface MybatisPlusMetaDataFiller extends MetaObjectHandler {

    /**
     * 创建时间字段的字段名
     *
     * @return 创建时间字段的字段名
     */
    String getCreateTimeFieldName();

    /**
     * 更新时间字段的字段名
     *
     * @return 更新时间字段的字段名
     */
    String getUpdateTimeFieldName();

    /**
     * 删除标识字段的字段名
     *
     * @return 删除标识字段的字段名
     */
    String getDeleteIsFieldName();

    /**
     * 插入表时填充属性
     *
     * @param metaObject
     */
    @Override
    default void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (needToFill(metaObject, getCreateTimeFieldName())) {
            this.setFieldValByName(getCreateTimeFieldName(), localDateTime, metaObject);
        }
        if (needToFill(metaObject, getUpdateTimeFieldName())) {
            this.setFieldValByName(getUpdateTimeFieldName(), localDateTime, metaObject);
        }
        if (needToFill(metaObject, getDeleteIsFieldName())) {
            this.setFieldValByName(getDeleteIsFieldName(), Boolean.FALSE, metaObject);
        }
    }

    /**
     * 更新表时填充属性
     *
     * @param metaObject
     */
    @Override
    default void updateFill(MetaObject metaObject) {
        if (needToFill(metaObject, getUpdateTimeFieldName())) {
            setFieldValByName(getUpdateTimeFieldName(), LocalDateTime.now(), metaObject);
        }
    }

    default boolean needToFill(MetaObject metaObject, String fieldName) {
        if (metaObject.hasSetter(fieldName)) {
            return Objects.isEmpty(metaObject.getValue(fieldName));
        }
        return false;
    }

}
