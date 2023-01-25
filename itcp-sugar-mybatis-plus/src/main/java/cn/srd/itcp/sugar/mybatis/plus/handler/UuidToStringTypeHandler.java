package cn.srd.itcp.sugar.mybatis.plus.handler;

import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

/**
 * 数据类型转换：{@link UUID} =&gt; {@link String}
 *
 * @author wjm
 * @since 2022-07-12
 */
public class UuidToStringTypeHandler extends BaseTypeHandler<UUID> {

    /**
     * 定义如何把 Java 类型的参数转换为指定的数据库类型
     *
     * @param preparedStatement SQL 预编译对象
     * @param columnIndex       字段索引
     * @param parameter         自定义参数
     * @param jdbcType          JDBC 数据类型
     */
    @Override
    @SneakyThrows
    public void setNonNullParameter(PreparedStatement preparedStatement, int columnIndex, UUID parameter, JdbcType jdbcType) {
        if (null != parameter) {
            preparedStatement.setObject(columnIndex, parameter.toString());
        }
    }

    /**
     * 定义通过字段名称获取字段数据时，如何把数据库类型转换为指定的 Java 类型
     *
     * @param resultSet  结果集
     * @param columnName 字段名称
     * @return 转换结果
     */
    @Override
    @SneakyThrows
    public UUID getNullableResult(ResultSet resultSet, String columnName) {
        return UUID.fromString(resultSet.getString(columnName));
    }

    /**
     * 定义通过字段索引获取字段数据时，如何把数据库类型转换为指定的 Java 类型
     *
     * @param resultSet   结果集
     * @param columnIndex 字段索引
     * @return 转换结果
     */
    @Override
    public UUID getNullableResult(ResultSet resultSet, int columnIndex) {
        return null;
    }

    /**
     * 定义通过存储过程获取字段数据时，如何把数据库类型转换为指定的 Java 类型
     *
     * @param callableStatement 存储过程执行对象
     * @param columnIndex       字段索引
     * @return 转换结果
     */
    @Override
    public UUID getNullableResult(CallableStatement callableStatement, int columnIndex) {
        return null;
    }

}
