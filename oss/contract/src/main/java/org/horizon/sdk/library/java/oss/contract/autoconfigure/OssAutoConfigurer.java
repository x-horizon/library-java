package org.horizon.sdk.library.java.oss.contract.autoconfigure;

import org.horizon.sdk.library.java.tool.enums.autoconfigure.EnableEnumAutowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import static org.horizon.sdk.library.java.oss.contract.autoconfigure.OssBasePackagePathAutoConfigurer.BASE_CONTRACT_PACKAGE_PATH;
import static org.horizon.sdk.library.java.oss.contract.autoconfigure.OssBasePackagePathAutoConfigurer.BASE_PACKAGE_PATH;

/**
 * {@link EnableAutoConfiguration AutoConfiguration} for Library Java Oss
 *
 * @author wjm
 * @since 2024-06-19 23:51
 */
@AutoConfiguration
@EnableEnumAutowired(scanPackagePaths = {BASE_PACKAGE_PATH, BASE_CONTRACT_PACKAGE_PATH})
public class OssAutoConfigurer {

}