package cn.srd.itcp.sugar.orm.mybatis.plus.common.database.mysql.utils;

import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 常用 SQL
 *
 * @author wjm
 * @since 2022-07-06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SQL {

    /**
     * SQL：将字节数组类型的主键解码为十六进制字符串
     *
     * @param poClass    表映射的实体类
     * @param primaryKey 主键
     * @param <PO>       PO 模型
     * @return primary_key_name = UNHEX('primaryKey');
     */
    public static <PO> String getDecodeHexPrimaryKey(Class<PO> poClass, Serializable primaryKey) {
        return TableInfoHelper.getTableInfo(poClass).getKeyColumn() + " = UNHEX('" + primaryKey + "')";
    }

}