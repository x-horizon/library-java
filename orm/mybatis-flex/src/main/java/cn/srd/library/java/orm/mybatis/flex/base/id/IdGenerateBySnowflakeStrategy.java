package cn.srd.library.java.orm.mybatis.flex.base.id;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.core.keygen.KeyGeneratorFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerateBySnowflakeStrategy implements IdGenerateStrategy {

    protected static final IdGenerateBySnowflakeStrategy INSTANCE = new IdGenerateBySnowflakeStrategy();

    @Override
    public String getGeneratorName() {
        return IdGenerateType.SNOWFLAKE_GENERATOR_NAME;
    }

    @Override
    public Class<? extends IdSnowflakeGenerator> getGenerator() {
        return IdSnowflakeGenerator.class;
    }

    @Override
    public FlexGlobalConfig.KeyConfig buildConfig(IdGenerateConfig idGenerateConfig) {
        KeyGeneratorFactory.register(getGeneratorName(), IdSnowflakeGenerator.INSTANCE);
        return IdGenerateStrategy.super.buildConfig(idGenerateConfig);
    }

}
