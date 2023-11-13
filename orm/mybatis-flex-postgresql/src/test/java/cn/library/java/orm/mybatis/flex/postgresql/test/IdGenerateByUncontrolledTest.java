// package cn.library.java.orm.mybatis.flex.postgresql.test;
//
// import cn.library.java.orm.mybatis.flex.postgresql.config.TestInsertListener;
// import cn.library.java.orm.mybatis.flex.postgresql.config.TestUpdateListener;
// import cn.library.java.orm.mybatis.flex.postgresql.dao.StudentTestIdUncontrolledDao;
// import cn.library.java.orm.mybatis.flex.postgresql.model.po.StudentTestIdUncontrolledPO;
// import cn.srd.library.java.contract.constant.booleans.BooleanConstant;
// import cn.srd.library.java.orm.mybatis.flex.base.autoconfigure.EnableMybatisFlexCustomizer;
// import cn.srd.library.java.orm.mybatis.flex.base.id.IdConfig;
// import cn.srd.library.java.orm.mybatis.flex.base.id.IdGenerateType;
// import cn.srd.library.java.orm.mybatis.flex.base.listener.ListenerConfig;
// import cn.srd.library.java.orm.mybatis.flex.base.logic.DeleteLogicConfig;
// import cn.srd.library.java.tool.lang.random.Randoms;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.mybatis.spring.annotation.MapperScan;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;
//
// @MapperScan("cn.library.java.orm.mybatis.flex.postgresql.dao")
// @EnableMybatisFlexCustomizer(
//         globalIdGenerateConfig = @IdConfig(generateType = IdGenerateType.UNCONTROLLED),
//         globalDeleteLogicConfig = @DeleteLogicConfig(normalValue = BooleanConstant.FALSE, deletedValue = BooleanConstant.TRUE),
//         globalListenerConfig = @ListenerConfig(whenInsert = TestInsertListener.class, whenUpdate = TestUpdateListener.class)
// )
// @RunWith(SpringRunner.class)
// @SpringBootTest
// public class IdGenerateByUncontrolledTest {
//
//     @Autowired private StudentTestIdUncontrolledDao studentTestIdUncontrolledDao;
//
//     @Test
//     public void testIt() {
//         // studentTestIdUncontrolledDao.insert(StudentTestIdUncontrolledPO.builder().id(Randoms.randomString(10)).build());
//         studentTestIdUncontrolledDao.insertSelective(StudentTestIdUncontrolledPO.builder().id(Randoms.randomString(10)).build());
//
//         System.out.println();
//     }
//
// }
