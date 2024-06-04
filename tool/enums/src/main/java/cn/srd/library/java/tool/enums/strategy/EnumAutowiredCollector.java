// Copyright (C) 2021-2026 thinkingto.com Ltd. All rights reserved.
// Use of this source code is governed by SRD.
// license that can be found in the LICENSE file.

package cn.srd.library.java.tool.enums.strategy;

import cn.srd.library.java.contract.constant.module.ModuleView;
import cn.srd.library.java.contract.constant.text.SuppressWarningConstant;
import cn.srd.library.java.contract.model.throwable.LibraryJavaInternalException;
import cn.srd.library.java.tool.enums.EnumAutowired;
import cn.srd.library.java.tool.enums.autoconfigure.EnableEnumAutowired;
import cn.srd.library.java.tool.lang.collection.Collections;
import cn.srd.library.java.tool.lang.compare.Comparators;
import cn.srd.library.java.tool.lang.enums.Enums;
import cn.srd.library.java.tool.lang.functional.Assert;
import cn.srd.library.java.tool.lang.object.Nil;
import cn.srd.library.java.tool.lang.reflect.Reflects;
import cn.srd.library.java.tool.spring.contract.Annotations;
import cn.srd.library.java.tool.spring.contract.Classes;
import cn.srd.library.java.tool.spring.contract.Springs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@link EnumAutowired} collector
 *
 * @author wjm
 * @since 2021-09-08 16:07
 */
@Slf4j
@Order(1)
public class EnumAutowiredCollector<E extends Enum<E>> implements SmartInitializingSingleton {

    @SuppressWarnings(SuppressWarningConstant.UNCHECKED)
    @Override
    public void afterSingletonsInstantiated() {
        log.info("{}enum autowired system is enabled, starting initializing...", ModuleView.TOOL_ENUM_SYSTEM);

        Set<String> scanPackagePaths = Classes.optimizeAnnotationAntStylePackagePaths(EnableEnumAutowired.class, "scanPackagePaths");
        Set<BeanDefinition> enumAutowiredBeanDefinitions = Classes.scanByAnnotationTypeFilter(EnumAutowired.class, scanPackagePaths);
        if (Nil.isEmpty(enumAutowiredBeanDefinitions)) {
            log.info("{}no class marked with [@{}].", ModuleView.TOOL_ENUM_SYSTEM, EnumAutowired.class.getName());
        }

        enumAutowiredBeanDefinitions.forEach(enumAutowiredBeanDefinition -> {
            Class<?> enumAutowiredAnnotatedClass = Classes.ofName(enumAutowiredBeanDefinition.getBeanClassName());
            Assert.of().setMessage("{}the class [{}] marked with [@{}] must be an enum class, please check!", ModuleView.TOOL_ENUM_SYSTEM, enumAutowiredAnnotatedClass.getName(), EnumAutowired.class.getName())
                    .setThrowable(LibraryJavaInternalException.class)
                    .throwsIfFalse(enumAutowiredAnnotatedClass.isEnum());

            EnumAutowired enumAutowired = Annotations.getAnnotation(enumAutowiredAnnotatedClass, EnumAutowired.class);
            String enumAutowiredAnnotatedClassName = enumAutowiredAnnotatedClass.getName();
            Arrays.stream(enumAutowired.rootClasses()).forEach(enumAutowiredRootClass -> {
                String enumAutowiredRootClassName = enumAutowiredRootClass.getName();
                Set<BeanDefinition> enumAutowiredChildrenClassDefinitions = Classes.scanByAssignableTypeFilter(enumAutowiredRootClass, scanPackagePaths);
                Assert.of().setMessage("{}the class [{}] marked with [@{}] bound interface [{}] has no implementation class, please check!", ModuleView.TOOL_ENUM_SYSTEM, enumAutowiredAnnotatedClassName, EnumAutowired.class.getName(), enumAutowiredRootClassName)
                        .setThrowable(LibraryJavaInternalException.class)
                        .throwsIfEmpty(enumAutowiredChildrenClassDefinitions);

                String autowiredFiledName = enumAutowired.autowiredFiledName();
                if (Nil.isBlank(autowiredFiledName)) {
                    List<Field> matchFields = Classes.getFields(enumAutowiredAnnotatedClass)
                            .stream()
                            .filter(enumAutowiredAnnotatedField -> Comparators.equals(enumAutowiredAnnotatedField.getType(), enumAutowiredRootClass))
                            .toList();
                    Assert.of().setMessage("{}the class [{}] marked with [@{}] has no field to match [{}], cannot autowired, please specified one!", ModuleView.TOOL_ENUM_SYSTEM, enumAutowiredAnnotatedClassName, EnumAutowired.class.getName(), enumAutowiredRootClassName)
                            .setThrowable(LibraryJavaInternalException.class)
                            .throwsIfEmpty(matchFields);
                    Assert.of().setMessage("{}the class [{}] marked with [@{}] has multi fields to match [{}], cannot autowired, please specified one!", ModuleView.TOOL_ENUM_SYSTEM, enumAutowiredAnnotatedClassName, EnumAutowired.class.getName(), enumAutowiredRootClassName)
                            .setThrowable(LibraryJavaInternalException.class)
                            .throwsIfTrue(matchFields.size() > 1);
                    autowiredFiledName = Collections.getFirst(matchFields).orElseThrow().getName();
                }

                Map<String, String> enumAutowiredSubclassSimpleNameMappingNameMap = enumAutowiredChildrenClassDefinitions.stream()
                        .map(enumAutowiredChildrenClassDefinition -> Classes.ofName(enumAutowiredChildrenClassDefinition.getBeanClassName()))
                        .map(enumAutowiredChildrenClass -> Collections.ofPair(enumAutowiredChildrenClass.getSimpleName(), enumAutowiredChildrenClass.getName()))
                        .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

                for (E enumField : Enums.getAllInstances((Class<E>) enumAutowiredAnnotatedClass)) {
                    EnumAutowiredFieldMatchRule enumAutowiredFieldMatchRule = Reflects.newInstance(enumAutowired.matchRule());
                    String theMostSuitableAutowiredClassSimpleName = enumAutowiredFieldMatchRule.getMostSuitableAutowiredClassName(enumField, Collections.getMapKeys(enumAutowiredSubclassSimpleNameMappingNameMap));
                    Object theMostSuitableAutowiredClass = Springs.getBean(Classes.ofName(enumAutowiredSubclassSimpleNameMappingNameMap.get(theMostSuitableAutowiredClassSimpleName)));
                    Assert.of().setMessage("{}find class [{}] and autowired it into enum [{}]-[{}] filed [{}], but the [{}] instance is null, you need to consider adding it to Spring IOC", ModuleView.TOOL_ENUM_SYSTEM, theMostSuitableAutowiredClassSimpleName, enumAutowiredAnnotatedClassName, enumField.name(), autowiredFiledName, theMostSuitableAutowiredClassSimpleName)
                            .setThrowable(LibraryJavaInternalException.class)
                            .throwsIfNull(theMostSuitableAutowiredClass);
                    Reflects.setFieldValue(enumField, autowiredFiledName, theMostSuitableAutowiredClass);
                    log.info("{}find class [{}] and autowired it into enum [{}]-[{}] filed [{}]", ModuleView.TOOL_ENUM_SYSTEM, theMostSuitableAutowiredClassSimpleName, enumAutowiredAnnotatedClassName, enumField.name(), autowiredFiledName);
                }
            });
        });

        log.info("{}enum autowired system initialized.", ModuleView.TOOL_ENUM_SYSTEM);
    }

}