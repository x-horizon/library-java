package cn.srd.library.java.studio.low.code.repository;

import cn.srd.library.java.orm.mybatis.flex.postgresql.repository.GenericRepository;
import cn.srd.library.java.studio.low.code.model.po.SchoolPO;
import org.springframework.stereotype.Repository;

/**
 * 学校信息 repository
 *
 * @author TODO 请填写作者名字
 * @since 2024-04-15 23:57
 */
@Repository
public interface SchoolRepository extends GenericRepository<SchoolPO> {

}