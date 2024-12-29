package cn.srd.library.java.orm.mybatis.flex.base.repository;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.constant.suppress.SuppressWarningConstant;
import cn.srd.library.java.contract.constant.text.SymbolConstant;
import cn.srd.library.java.contract.model.base.PO;
import cn.srd.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.srd.library.java.orm.mybatis.flex.base.cache.MybatisFlexSystemCache;
import cn.srd.library.java.orm.mybatis.flex.base.chain.DeleteChainer;
import cn.srd.library.java.orm.mybatis.flex.base.chain.QueryChain;
import cn.srd.library.java.orm.mybatis.flex.base.chain.QueryChainer;
import cn.srd.library.java.orm.mybatis.flex.base.chain.UpdateChainer;
import cn.srd.library.java.orm.mybatis.flex.base.support.ColumnNameGetter;
import cn.srd.library.java.orm.mybatis.flex.base.support.MybatisFlexs;
import cn.srd.library.java.tool.lang.collection.Collections;
import cn.srd.library.java.tool.lang.convert.Converts;
import cn.srd.library.java.tool.lang.functional.Action;
import cn.srd.library.java.tool.lang.object.Nil;
import cn.srd.library.java.tool.lang.reflect.Reflects;
import cn.srd.library.java.tool.lang.text.Strings;
import cn.srd.library.java.tool.spring.contract.support.Springs;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.keygen.CustomKeyGenerator;
import com.mybatisflex.core.logicdelete.LogicDeleteManager;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.core.util.ClassUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * the generic curd repository
 *
 * @param <P> the entity extends {@link PO}
 * @author wjm
 * @since 2023-11-04 00:19
 */
@SuppressWarnings(SuppressWarningConstant.UNUSED)
@CanIgnoreReturnValue
public interface GenericRepository<P extends PO> {

    /**
     * see <a href="https://mybatis-flex.com/zh/base/batch.html">"the batch operation guide"</a>.
     */
    int GENERATE_FULL_SQL_BATCH_SIZE = 100;

    /**
     * default batch operation size each time
     */
    int DEFAULT_BATCH_SIZE_EACH_TIME = BaseMapper.DEFAULT_BATCH_SIZE;

    /**
     * insert and not append the null column value.
     * <ul>
     *   <li>if the value of primary key is null or blank string, will be generated by primary key generate strategy.</li>
     *   <li>if the value of primary key is not null or not blank string, then using the primary key value in entity.</li>
     *   <li>supported the entity with multiple primary keys.</li>
     * </ul>
     * for example:
     * <ul>
     *   <li>
     *       the entity: TestTablePO(id=null, name="testName")<br/>
     *       the generated sql: INSERT INTO "test_table"("id", "name") VALUES (487223443892741, 'testName');
     *   </li>
     *   <br/>
     *   <li>
     *       the entity: TestTablePO(id=null, name=null)<br/>
     *       the generated sql: INSERT INTO "test_table"("id") VALUES (487223443892741);
     *   </li>
     *   <br/>
     *   <li>
     *       the entity: TestTablePO(id=1, name=null)<br/>
     *       the generated sql: INSERT INTO "test_table"("id") VALUES (1);
     *   </li>
     *   <br/>
     *   <li>
     *       the entity with two primary keys: TestTablePO(id1=null, id2=null).<br/>
     *       the generated sql: INSERT INTO "test_table"("id1", "id2") VALUES (487223443892741, 487223443892742);
     *   </li>
     *   <br/>
     *   <li>
     *       the entity with two primary keys: TestTablePO(id1=1L, id2=2L).<br/>
     *       the generated sql: INSERT INTO "test_table"("id1", "id2") VALUES (1, 2);
     *   </li>
     * </ul>
     *
     * @param entity the operate entity
     * @return the entity with primary key
     * @see BaseMapper#insertSelective(Object)
     * @see CustomKeyGenerator#processBefore(Executor, MappedStatement, Statement, Object)
     */
    default P save(P entity) {
        getBaseMapper().insertSelective(entity);
        return entity;
    }

    /**
     * using {@link #GENERATE_FULL_SQL_BATCH_SIZE batch size each time} to insert batch.
     *
     * @param entities the operate entities
     * @return the entities with primary key
     * @see #GENERATE_FULL_SQL_BATCH_SIZE
     * @see #save(PO)
     * @see #saveBatch(Iterable, int)
     * @see BaseMapper#insertSelective(Object)
     * @see BaseMapper#insertBatch(List, int)
     * @see IService#saveBatch(Collection, int)
     * @see CustomKeyGenerator#processBefore(Executor, MappedStatement, Statement, Object)
     */
    @Transactional(rollbackFor = Throwable.class)
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> saveBatch(Iterable<P> entities) {
        return Springs.getProxy(GenericRepository.class).saveBatch(entities, DEFAULT_BATCH_SIZE_EACH_TIME);
    }

    /**
     * insert batch.
     * <ol>
     *   <li>
     *       using {@link BaseMapper#insertBatch(List, int)} if the entities size <= {@link #GENERATE_FULL_SQL_BATCH_SIZE}, the generated insert sql like:<br/>
     *       <br/>
     *       INSERT INTO "test_table"("id", "name") VALUES<br/>
     *       (487223443892741, 'test1'),<br/>
     *       (487223443892742, 'test2'),<br/>
     *       (487223443913230, 'test3');
     *   </li>
     *   <br/>
     *   <li>
     *       using {@link IService#saveBatch(Collection, int)} if the entities size > {@link #GENERATE_FULL_SQL_BATCH_SIZE}, the generated insert sql like:<br/>
     *       <br/>
     *       INSERT INTO "test_table"("id", "name") VALUES (487223443892741, 'test1');<br/>
     *       INSERT INTO "test_table"("id", "name") VALUES (487223443892742, 'test2');<br/>
     *       INSERT INTO "test_table"("id", "name") VALUES (487223443913230, 'test3');
     *   </li>
     * </ol>
     *
     * @param entities          the operate entities
     * @param batchSizeEachTime insert size each time
     * @return the entities with primary key
     * @apiNote about the different between {@link BaseMapper#insertBatch(List, int)} and {@link IService#saveBatch(Collection, int)}, you should see <a href="https://mybatis-flex.com/zh/base/batch.html">"the batch operation guide"</a>.
     * @see #GENERATE_FULL_SQL_BATCH_SIZE
     * @see #save(PO)
     * @see BaseMapper#insertSelective(Object)
     * @see BaseMapper#insertBatch(List, int)
     * @see IService#saveBatch(Collection, int)
     * @see CustomKeyGenerator#processBefore(Executor, MappedStatement, Statement, Object)
     */
    @Transactional(rollbackFor = Throwable.class)
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> saveBatch(Iterable<P> entities, int batchSizeEachTime) {
        if (Nil.isEmpty(entities)) {
            return Collections.newArrayList();
        }
        List<P> listTypeEntities = entities instanceof List<P> actualEntities ? actualEntities : Converts.toList(entities);
        Action.ifTrue(listTypeEntities.size() <= GENERATE_FULL_SQL_BATCH_SIZE)
                .then(() -> getBaseMapper().insertBatch(listTypeEntities, batchSizeEachTime))
                .otherwise(() -> Db.executeBatch(listTypeEntities, batchSizeEachTime, ClassUtil.getUsefulClass(this.getClass()), GenericRepository::save));
        return listTypeEntities;
    }

    /**
     * update by id.
     *
     * @param entity – the operate entity
     * @see #updateBatchById(Iterable, int)
     */
    default P updateById(P entity) {
        getBaseMapper().update(entity);
        return entity;
    }

    default P updateByIdIgnoreLogicDelete(P entity) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateById(entity));
    }

    /**
     * update batch by id.
     *
     * @param entities the operate entities
     * @see #updateBatchById(Iterable, int)
     * @see IService#updateBatch(Collection, int)
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchById(P... entities) {
        List<P> needToUpdateEntities = Collections.ofArrayList(entities);
        Springs.getProxy(GenericRepository.class).updateBatchById(needToUpdateEntities);
        return needToUpdateEntities;
    }

    /**
     * using {@link #GENERATE_FULL_SQL_BATCH_SIZE batch size each time} to update by id.
     *
     * @param entities the operate entities
     * @see #updateBatchById(Iterable, int)
     * @see IService#updateBatch(Collection, int)
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchById(Iterable<P> entities) {
        return Springs.getProxy(GenericRepository.class).updateBatchById(entities, DEFAULT_BATCH_SIZE_EACH_TIME);
    }

    /**
     * update batch by id.
     * <ul>
     *   <li>using {@link IService#updateBatch(Collection, int)} to update.</li>
     *   <li>supported the entity with multiple primary keys.</li>
     * </ul>
     * <ul>
     *   <li>for example:</li>
     *   <ul>
     *     <li>
     *         if the entity has one primary key, the generated insert sql like:<br/><br/>
     *         UPDATE "test_table" SET "name" = 'test1' WHERE "id" = 1;
     *     </li>
     *     <br/>
     *     <li>
     *         if the entity has two primary keys, the generated insert sql like:<br/><br/>
     *         UPDATE "test_table" SET "name" = 'test1' WHERE "id" = 1 AND "id2" = 2;
     *     </li>
     *   </ul>
     * </ul>
     *
     * @param entities          the operate entities
     * @param batchSizeEachTime insert size each time
     * @apiNote see <a href="https://mybatis-flex.com/zh/base/batch.html">"the batch operation guide"</a>.
     * @see IService#updateBatch(Collection, int)
     */
    @Transactional(rollbackFor = Throwable.class)
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchById(Iterable<P> entities, int batchSizeEachTime) {
        if (Nil.isEmpty(entities)) {
            return Collections.newArrayList();
        }
        List<P> needToUpdateEntities = entities instanceof List<P> actualEntities ? actualEntities : Converts.toList(entities);
        Db.executeBatch(
                needToUpdateEntities,
                batchSizeEachTime,
                ClassUtil.getUsefulClass(this.getClass()),
                GenericRepository::updateById
        );
        return needToUpdateEntities;
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchByIdIgnoreLogicDelete(P... entities) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateBatchById(entities));
    }

    default List<P> updateBatchByIdIgnoreLogicDelete(Iterable<P> entities) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateBatchById(entities));
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchByIdIgnoreLogicDelete(Iterable<P> entities, int batchSizeEachTime) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> Springs.getProxy(GenericRepository.class).updateBatchById(entities, batchSizeEachTime));
    }

    /**
     * update with version by id.
     * <ul>
     *   <li>supported the entity with version.</li>
     *   <li>supported the entity with multiple primary keys.</li><br/>
     *   <li>the update stage as following:</li>
     *   <ul>
     *     <li>the entity with one primary key: TestTablePO(id=1L, name="test1", version=0).</li>
     *     <ol>
     *       <li>SELECT * FROM "test_table" WHERE "id" = 1;</li>
     *       <li>UPDATE "test_table" SET "name" = 'test2', "version" = "version" + 1  WHERE "id" = 1 AND "version" = 0;</li>
     *     </ol>
     *   </ul>
     *   <ul>
     *     <li>the entity with two primary key: TestTablePO(id1=1L, id2=2L, name="test1", version=0).</li>
     *     <ol>
     *       <li>SELECT * FROM "test_table" WHERE "id" = 1 AND "id2" = 2;</li>
     *       <li>UPDATE "test_table" SET "name" = 'test2', "version" = "version" + 1  WHERE "id" = 1 AND "id2" = 2 AND "version" = 0;</li>
     *     </ol>
     *   </ul>
     * </ul>
     *
     * @param entity the operate entity
     * @see #updateById(PO)
     */
    default P updateWithVersionById(P entity) {
        setVersionFieldValue(getEntityToUpdateVersion(entity), entity);
        updateById(entity);
        return entity;
    }

    default P updateWithVersionByIdIgnoreLogicDelete(P entity) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateWithVersionById(entity));
    }

    /**
     * update batch with version by id.
     *
     * @param entities    the operate entities
     * @param getIdAction how to find the primary key in each entity
     * @apiNote only support the entity with one primary key.
     * @see #updateBatchWithVersionById(Iterable, Function, int)
     * @see #updateBatchById(Iterable, int)
     * @see IService#updateBatch(Collection, int)
     */
    default List<P> updateBatchWithVersionById(Iterable<P> entities, Function<P, ? extends Serializable> getIdAction) {
        setVersionFieldValues(entities, getIdAction);
        return updateBatchById(entities);
    }

    /**
     * update batch by with version id.
     * <ul>
     *   <li>supported the entity with version.</li>
     *   <li>only support the entity with one primary key.</li><br/>
     *   <li>the update stage as following:</li>
     *   <ul>
     *     <li>the entities with one primary key: [TestTablePO(id=1L, name="test1", version=0), PestTablePO(id=2L, name="test2", version=0)].</li>
     *     <ol>
     *       <li>SELECT * FROM "test_table" WHERE "id" = 1  OR "id" = 2;</li>
     *       <li>UPDATE "test_table" SET "name" = 'test3', "version" = "version" + 1  WHERE "id" = 1 AND "version" = 0;</li>
     *       <li>UPDATE "test_table" SET "name" = 'test4', "version" = "version" + 1  WHERE "id" = 2 AND "version" = 0;</li>
     *     </ol>
     *   </ul>
     * </ul>
     *
     * @param entities          the operate entities
     * @param getIdAction       how to find the primary key in each entity
     * @param batchSizeEachTime insert size each time
     * @apiNote only support the entity with one primary key.
     * @see #updateBatchById(Iterable, int)
     * @see IService#updateBatch(Collection, int)
     */
    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    default List<P> updateBatchWithVersionById(Iterable<P> entities, Function<P, ? extends Serializable> getIdAction, int batchSizeEachTime) {
        setVersionFieldValues(entities, getIdAction);
        return Springs.getProxy(GenericRepository.class).updateBatchById(entities, batchSizeEachTime);
    }

    default List<P> updateBatchWithVersionByIdIgnoreLogicDelete(Iterable<P> entities, Function<P, ? extends Serializable> getIdAction) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateBatchWithVersionById(entities, getIdAction));
    }

    default List<P> updateBatchWithVersionByIdIgnoreLogicDelete(Iterable<P> entities, Function<P, ? extends Serializable> getIdAction, int batchSizeEachTime) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> updateBatchWithVersionById(entities, getIdAction, batchSizeEachTime));
    }

    /**
     * delete by id, recommended for the entity with multiple primary keys.
     * <ol>
     *   <li>
     *       if the entity has the logic delete column:<br/>
     *       <ul>
     *         <li>
     *              the entity with two primary keys: TestTablePO(id1=1L, id2=2L, rowIsDeleted=null).
     *         </li>
     *         <li>
     *              the generated delete sql like:<br/><br/>
     *              UPDATE "test_table"<br/>
     *              SET "row_is_deleted" = TRUE<br/>
     *              WHERE "id1" = 1 AND "id2" = 2 AND "row_is_deleted" = FALSE;
     *         </li>
     *       </ul>
     *   </li>
     *   <br/>
     *   <li>
     *       if the entity does not have the logic delete column:<br/>
     *       <ul>
     *         <li>
     *              the entity with two primary keys: TestTablePO(id1=1L, id2=2L).
     *         </li>
     *         <li>
     *              the generated delete sql like:<br/><br/>
     *              DELETE<br/>
     *              FROM "test_table"<br/>
     *              WHERE "id1" = 1 AND "id2" = 2 AND "row_is_deleted" = FALSE;
     *         </li>
     *       </ul>
     *   </li>
     * </ol>
     *
     * @param entity the entity
     */
    default void deleteById(P entity) {
        getBaseMapper().delete(entity);
    }

    default void deleteById(Serializable id) {
        getBaseMapper().deleteById(id);
    }

    /**
     * delete ignore logic delete, recommended for the entity with multiple primary keys.
     * <ul>
     *   <li>
     *       no matter of whether the entity has the logic delete column:<br/>
     *       <ul>
     *         <li>
     *              the entity with two primary keys: TestTablePO(id1=1L, id2=2L, rowIsDeleted=null).
     *         </li>
     *         <li>
     *              the generated delete sql like:<br/><br/>
     *              DELETE<br/>
     *              FROM "test_table"<br/>
     *              WHERE "id1" = 1 AND "id2" = 2 AND "row_is_deleted" = FALSE;
     *         </li>
     *       </ul>
     *   </li>
     * </ul>
     *
     * @param entity the entity
     */
    default void deleteByIdIgnoreLogicDelete(P entity) {
        LogicDeleteManager.execWithoutLogicDelete(() -> deleteById(entity));
    }

    /**
     * delete ignore logic delete.
     *
     * @param id the primary key value
     * @see #deleteByIdsIgnoreLogicDelete(Iterable)
     */
    default void deleteByIdIgnoreLogicDelete(Serializable id) {
        LogicDeleteManager.execWithoutLogicDelete(() -> deleteById(id));
    }

    /**
     * delete batch by ids.
     *
     * @param ids the primary key values
     * @see #deleteByIds(Iterable)
     */
    default void deleteByIds(Serializable... ids) {
        deleteByIds(Collections.ofHashSet(ids));
    }

    /**
     * delete batch by ids.
     * <ol>
     *   <li>
     *       if the entity has the logic delete column, the generated delete sql like:<br/>
     *       <br/>
     *       UPDATE "test_table"<br/>
     *       SET "row_is_deleted" = TRUE<br/>
     *       WHERE ("id" = 1 OR "id" = 2 OR "id" = 3)<br/>
     *       AND "row_is_deleted" = FALSE;
     *   </li>
     *   <br/>
     *   <li>
     *       if the entity does not have the logic delete column, the generated delete sql like:<br/>
     *       <br/>
     *       DELETE<br/>
     *       FROM "test_table"<br/>
     *       WHERE "id" = 1 OR "id" = 2 OR "id" = 3;
     *   </li>
     * </ol>
     *
     * @param ids the primary key values
     */
    default void deleteByIds(Iterable<? extends Serializable> ids) {
        if (Nil.isEmpty(ids)) {
            return;
        }
        getBaseMapper().deleteBatchByIds(ids instanceof Collection<? extends Serializable> ? (Collection<? extends Serializable>) ids : Converts.toSet(ids));
    }

    /**
     * delete batch ignore logic delete.
     *
     * @param ids the primary key values
     * @see #deleteByIdsIgnoreLogicDelete(Iterable)
     */
    default void deleteByIdsIgnoreLogicDelete(Serializable... ids) {
        deleteByIdsIgnoreLogicDelete(Collections.ofHashSet(ids));
    }

    /**
     * delete batch ignore logic delete.
     * <ul>
     *   <li>
     *       no matter of whether the entity has the logic delete column, the generated delete sql like:<br/>
     *       <br/>
     *       DELETE<br/>
     *       FROM "test_table"<br/>
     *       WHERE "id" = 1 OR "id" = 2 OR "id" = 3;
     *   </li>
     * </ul>
     *
     * @param ids the primary key values
     */
    default void deleteByIdsIgnoreLogicDelete(Iterable<? extends Serializable> ids) {
        LogicDeleteManager.execWithoutLogicDelete(() -> deleteByIds(ids instanceof Collection<? extends Serializable> ? ids : Converts.toSet(ids)));
    }

    default Optional<P> getById(Serializable id) {
        return Optional.ofNullable(getBaseMapper().selectOneById(id));
    }

    default Optional<P> getById(P entity) {
        return Optional.ofNullable(getBaseMapper().selectOneByEntityId(entity));
    }

    default Optional<P> getByIdIgnoreLogicDelete(Serializable id) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> getById(id));
    }

    default Optional<P> getByIdIgnoreLogicDelete(P entity) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> getById(entity));
    }

    default Optional<P> getByField(ColumnNameGetter<P> columnNameGetter, Object value) {
        return Optional.ofNullable(getBaseMapper().selectOneByMap(Collections.ofImmutableMap(MybatisFlexs.getColumnName(columnNameGetter), value)));
    }

    default Optional<P> getByFieldIgnoreLogicDelete(ColumnNameGetter<P> columnNameGetter, Object value) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> getByField(columnNameGetter, value));
    }

    default List<P> listByIds(Iterable<? extends Serializable> ids) {
        if (Nil.isEmpty(ids)) {
            return Collections.newArrayList();
        }
        return getBaseMapper().selectListByIds(ids instanceof Collection<? extends Serializable> ? (Collection<? extends Serializable>) ids : Converts.toSet(ids));
    }

    default List<P> listByIdsIgnoreLogicDelete(Iterable<? extends Serializable> ids) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> listByIdsIgnoreLogicDelete(ids));
    }

    default List<P> listByField(ColumnNameGetter<P> columnNameGetter, Object value) {
        return getBaseMapper().selectListByMap(Collections.ofImmutableMap(MybatisFlexs.getColumnName(columnNameGetter), value));
    }

    default List<P> listByFieldIgnoreLogicDelete(ColumnNameGetter<P> columnNameGetter, Object value) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> listByFieldIgnoreLogicDelete(columnNameGetter, value));
    }

    default List<P> listLikeByField(ColumnNameGetter<P> columnNameGetter, String value) {
        return getBaseMapper().selectListByQuery(QueryWrapper.create().like(MybatisFlexs.getColumnName(columnNameGetter), Nil.isNull(value) ? SymbolConstant.EMPTY : value));
    }

    default List<P> listLikeByFieldIgnoreLogicDelete(ColumnNameGetter<P> columnNameGetter, String value) {
        return LogicDeleteManager.execWithoutLogicDelete(() -> listLikeByField(columnNameGetter, value));
    }

    default List<P> listAll() {
        return getBaseMapper().selectListByQuery(QueryWrapper.create());
    }

    default List<P> listAllIgnoreLogicDelete() {
        return LogicDeleteManager.execWithoutLogicDelete(this::listAll);
    }

    default long countAll() {
        return getBaseMapper().selectCountByQuery(QueryWrapper.create());
    }

    default long countAllIgnoreLogicDelete() {
        return LogicDeleteManager.execWithoutLogicDelete(this::countAll);
    }

    default QueryChainer<P> openQuery() {
        BaseMapper<P> baseMapper = getBaseMapper();
        return new QueryChainer<>(QueryChain.of(baseMapper));
    }

    default UpdateChainer<P> openUpdate() {
        BaseMapper<P> baseMapper = getBaseMapper();
        return new UpdateChainer<>(UpdateChain.of(baseMapper));
    }

    default DeleteChainer<P> openDelete() {
        BaseMapper<P> baseMapper = getBaseMapper();
        return new DeleteChainer<>(UpdateChain.of(baseMapper));
    }

    default BaseMapper<P> getBaseMapper() {
        return MybatisFlexSystemCache.getInstance().getBaseMapper(this.getClass());
    }

    private P getEntityToUpdateVersion(P updatedEntity) {
        return getById(updatedEntity).orElseThrow(() -> new LibraryJavaInternalException(Strings.format("{}update with version failed, the entity [{}] could not be found in table [{}], please check!", ModuleView.ORM_MYBATIS_SYSTEM, updatedEntity.getClass().getName(), MybatisFlexs.getTableName(updatedEntity).orElse(null))));
    }

    private void setVersionFieldValue(P oldEntity, P updatedEntity) {
        String versionFieldName = MybatisFlexs.getVersionFieldName(updatedEntity).orElseThrow(() -> new LibraryJavaInternalException(Strings.format("{}update with version failed, the entity [{}] does not have the version column, please check!", ModuleView.ORM_MYBATIS_SYSTEM, updatedEntity.getClass().getName())));
        Reflects.setFieldValue(updatedEntity, versionFieldName, Reflects.getFieldValue(oldEntity, versionFieldName));
    }

    private void setVersionFieldValues(Iterable<P> updatedEntities, Function<P, ? extends Serializable> getIdAction) {
        Map<? extends Serializable, P> idMappingOldEntity = Converts.toMap(listByIds(Converts.toList(updatedEntities, getIdAction)), getIdAction);
        Map<? extends Serializable, P> idMappingUpdatedEntity = Converts.toMap(updatedEntities, getIdAction);
        idMappingOldEntity.forEach((id, oldEntity) -> setVersionFieldValue(oldEntity, idMappingUpdatedEntity.get(id)));
    }

}