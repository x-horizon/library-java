package cn.srd.library.java.contract.component.oss.model.enums;

import cn.srd.library.java.contract.component.oss.strategy.OssStorage;
import cn.srd.library.java.tool.enums.EnumAutowired;
import cn.srd.library.java.tool.enums.strategy.EnumAutowiredFieldMatchByContainIgnoreCaseRule;
import lombok.Getter;

/**
 * @author wjm
 * @since 2024-07-16 19:45
 */
@Getter
@EnumAutowired(rootClasses = OssStorage.class, allowNull = true, matchRule = EnumAutowiredFieldMatchByContainIgnoreCaseRule.class)
public enum OssType {

    LOCAL(1, "local"),
    MINIO(2, "minio"),

    ;

    OssType(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private final int code;

    private final String value;

    private OssStorage storage;

}