// Copyright (C) 2021-2023 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.orm.mybatis.flex.base.cache;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.constant.text.SuppressWarningConstant;
import cn.srd.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.srd.library.java.orm.contract.model.base.PO;
import cn.srd.library.java.orm.mybatis.flex.base.repository.GenericRepository;
import cn.srd.library.java.tool.lang.annotation.Annotations;
import cn.srd.library.java.tool.lang.collection.Collections;
import cn.srd.library.java.tool.lang.functional.Assert;
import cn.srd.library.java.tool.lang.object.Types;
import cn.srd.library.java.tool.lang.text.Strings;
import cn.srd.library.java.tool.spring.contract.Classes;
import cn.srd.library.java.tool.spring.contract.Springs;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.BaseMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wjm
 * @since 2023-12-18 23:34
 */
public class MybatisFlexSystemCache<P extends PO, R extends GenericRepository<P>, B extends BaseMapper<P>> implements SmartInitializingSingleton {

    private final Map<Class<R>, MybatisFlexSystemCacheDTO<P, R>> repositoryClassMappingSystemCacheMap = Collections.newConcurrentHashMap(256);

    private final Map<Class<R>, MybatisFlexSystemCacheDTO<P, R>> repositoryProxyClassMappingSystemCacheMap = Collections.newConcurrentHashMap(256);

    @Getter private static MybatisFlexSystemCache<?, ?, ?> instance = null;

    @PostConstruct
    public void initialize() {
        instance = this;
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Class<R>> repositoryClassNameMappingRepositoryClassMap = Classes.scanBySuper(GenericRepository.class)
                .stream()
                .map(repositoryClass -> Collections.ofPair(repositoryClass.getSimpleName(), repositoryClass))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, entry -> (Class<R>) entry.getValue()));

        repositoryClassMappingSystemCacheMap.putAll(repositoryClassNameMappingRepositoryClassMap.values()
                .stream()
                .map(repositoryClass -> Collections.ofPair(repositoryClass, MybatisFlexSystemCacheDTO.<P, R>builder().repositoryClass(repositoryClass).build()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
        );

        Classes.scanBySuper(BaseMapper.class)
                .stream()
                .map(baseMapperClass -> Collections.ofPair(baseMapperClass.getSimpleName(), baseMapperClass))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, entry -> (Class<B>) entry.getValue()))
                .entrySet()
                .stream()
                .map(entry -> Collections.ofPair(repositoryClassNameMappingRepositoryClassMap.get(Strings.getMostSimilar(entry.getKey(), repositoryClassNameMappingRepositoryClassMap.keySet())).getName(), Springs.getBean(entry.getValue())))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue))
                .forEach((repositoryClassName, baseMapper) -> {
                    Class<R> repositoryProxyClass = (Class<R>) Classes.ofName(repositoryClassName);
                    Class<P> poClass = (Class<P>) Types.getClassGenericType(repositoryProxyClass);
                    Assert.of().setMessage("{}could not find the po class by repository class: [{}], please check if your repository is marked with po generic!", ModuleView.ORM_MYBATIS_SYSTEM, repositoryClassName)
                            .setThrowable(LibraryJavaInternalException.class)
                            .throwsIfNull(poClass);
                    String tableName = Annotations.getAnnotationValue(poClass, Table.class);
                    Assert.of().setMessage("{}could not find the table name by po class [{}], please check the annotation [{}] is marked!", ModuleView.ORM_MYBATIS_SYSTEM, poClass.getName(), Table.class.getName())
                            .setThrowable(LibraryJavaInternalException.class)
                            .throwsIfNull(tableName);
                    repositoryClassMappingSystemCacheMap.put(repositoryProxyClass, repositoryClassMappingSystemCacheMap
                            .get(repositoryProxyClass)
                            .setBaseMapper(baseMapper)
                            .setPoClass(poClass)
                            .setTableName(tableName)
                    );
                });
    }

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    public <P1 extends PO, R1 extends GenericRepository<P1>> MybatisFlexSystemCacheDTO<P1, R1> get(Class<?> repositoryProxyClass) {
        MybatisFlexSystemCacheDTO<P1, R1> systemCacheDTO = (MybatisFlexSystemCacheDTO<P1, R1>) repositoryProxyClassMappingSystemCacheMap.get(repositoryProxyClass);
        Assert.of().setMessage("{}could not find the mybatis flex system cache by repository, please using this function before get the base mapper first!", ModuleView.ORM_MYBATIS_SYSTEM)
                .setThrowable(LibraryJavaInternalException.class)
                .throwsIfNull(systemCacheDTO);
        return systemCacheDTO;
    }

    @SuppressWarnings({SuppressWarningConstant.UNCHECKED, SuppressWarningConstant.RAW_TYPE})
    public <P1 extends PO> BaseMapper<P1> getBaseMapper(Class repositoryProxyClass) {
        return repositoryProxyClassMappingSystemCacheMap.computeIfAbsent(
                        repositoryProxyClass,
                        ignore -> repositoryClassMappingSystemCacheMap
                                .get(Classes.ofName(Arrays.stream(repositoryProxyClass.getAnnotatedInterfaces()).findAny().orElseThrow().getType().getTypeName()))
                                .setRepositoryProxyClass(repositoryProxyClass)
                )
                .getBaseMapper();
    }

}