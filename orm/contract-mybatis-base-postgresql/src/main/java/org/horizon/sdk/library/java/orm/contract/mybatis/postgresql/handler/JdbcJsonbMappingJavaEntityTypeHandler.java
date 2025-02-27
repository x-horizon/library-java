package org.horizon.sdk.library.java.orm.contract.mybatis.postgresql.handler;

/**
 * <pre>
 * the postgresql jdbc jsonb data type and java entity mapping relation type handler.
 *
 * 1. the postgresql sql contain jsonb like map {} as following:
 * {@code
 *     CREATE TABLE example
 *     (
 *         id          BIGINT                     NOT NULL,
 *         detail_info JSONB  DEFAULT '{}'::JSONB NOT NULL, -- the value like {"name": "myName", "age": 18}
 *         PRIMARY KEY (id)
 *     );
 * }
 *
 * 2. the java object as following:
 * {@code
 *     @Data
 *     // need to replace this annotation from the specified orm framework
 *     @OrmFrameworkTableMarkedDemo(tableName = "example")
 *     public class ExamplePO implements Serializable {
 *
 *         @Serial private static final long serialVersionUID = -7680901283684311918L;
 *
 *         // need to replace this annotation from the specified orm framework
 *         @OrmFrameworkIdMarkedDemo
 *         @OrmFrameworkColumnMarkedDemo(columnName = "id")
 *         private Long id;
 *
 *         // need to replace this annotation from the specified orm framework
 *         // add the type handler
 *         @OrmFrameworkColumnMarkedDemo(columnName = "detail_info", typeHandler = JdbcJsonbMappingJavaEntityTypeHandler.class)
 *         private DetailPO detailPO;
 *
 *     }
 * }
 *
 * 3. the java object mapping postgresql jdbc jsonb as following:
 * {@code
 *     @Data
 *     public class DetailPO implements Serializable {
 *
 *         @Serial private static final long serialVersionUID = -88531220073385451L;
 *
 *         private String name;
 *
 *         private Short age;
 *
 *     }
 * }
 * </pre>
 *
 * <h2>note: the core of the postgresql jdbc jsonb data type and java entity mapping relation is:</h2>
 * <strong><em>@OrmFrameworkColumnMarkedDemo(columnName = "detail_info", typeHandler = JdbcJsonbMappingJavaEntityTypeHandler.class)</em></strong>
 * <p>
 *
 * @param <T> the java object data type
 * @author wjm
 * @since 2023-11-08 16:51
 */
public class JdbcJsonbMappingJavaEntityTypeHandler<T> extends AbstractJdbcJsonbMappingJavaObjectTypeHandler<T> {

}