package org.horizon.sdk.library.java.tool.spring.contract.support;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.horizon.sdk.library.java.contract.constant.annotation.AnnotationConstant;
import org.horizon.sdk.library.java.contract.constant.classes.ClassConstant;
import org.horizon.sdk.library.java.contract.constant.module.ModuleView;
import org.horizon.sdk.library.java.contract.constant.text.SymbolConstant;
import org.horizon.sdk.library.java.contract.constant.web.ProtocolConstant;
import org.horizon.sdk.library.java.contract.model.throwable.LibraryJavaInternalException;
import org.horizon.sdk.library.java.tool.lang.collection.Collections;
import org.horizon.sdk.library.java.tool.lang.object.BasePackagePath;
import org.horizon.sdk.library.java.tool.lang.object.Nil;
import org.horizon.sdk.library.java.tool.lang.text.Strings;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * toolkit for class
 *
 * @author wjm
 * @since 2023-10-06 10:48
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classes extends org.horizon.sdk.library.java.tool.lang.object.Classes {

    private static final PathMatchingResourcePatternResolver PATH_MATCHING_RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    public static Class<?> ofName(String className) {
        return ofName(className, getDefaultClassLoader());
    }

    public static Class<?> ofName(String className, ClassLoader classLoader) {
        return ClassUtils.resolveClassName(className, classLoader);
    }

    public static ClassLoader getDefaultClassLoader() {
        return ClassUtils.getDefaultClassLoader();
    }

    /**
     * return the base package path in spring project
     *
     * @return the base package path in spring project
     * @see Springs#getSpringBootApplicationPackagePath()
     * @see BasePackagePath#get()
     */
    public static Set<String> getBasePackagePath() {
        return BasePackagePath.register(Springs.getSpringBootApplicationPackagePath());
    }

    /**
     * get resources by ant style class path
     *
     * @param antStyleClassPath the ant style class path
     * @return the set of {@link Resource}
     * @apiNote if the resource url is a jar url,
     * then call the method {@link PathMatchingResourcePatternResolver#doFindPathMatchingJarResources(Resource, URL, String) doFindPathMatchingJarResources} to {@link AntPathMatcher#match(String, String) match} two paths like "&#42;/xx1/xx2", "test/xx1/xx2/",
     * it will always match failed because only match success by two paths like "&#42;/xx1/xx2/", "test/xx1/xx2/",
     * so in this way, will add prefix "/" to the ant style class path and {@link PathMatchingResourcePatternResolver#getResources(String) get resource} again to avoid this case.
     * @see AntPathMatcher
     * @see PathMatchingResourcePatternResolver#getResources(String)
     */
    public static Set<Resource> getResourceByAntStyleClassPath(String antStyleClassPath) {
        Set<Resource> resources = Arrays.stream(Try.of(() -> PATH_MATCHING_RESOURCE_PATTERN_RESOLVER.getResources(antStyleClassPath)).get()).collect(Collectors.toSet());
        if (Nil.isEmpty(resources) && Strings.notEndWith(antStyleClassPath, SymbolConstant.SLASH)) {
            resources = Arrays.stream(Try.of(() -> PATH_MATCHING_RESOURCE_PATTERN_RESOLVER.getResources(antStyleClassPath + SymbolConstant.SLASH)).get()).collect(Collectors.toSet());
        }
        return resources;
    }

    /**
     * <pre>
     * get the largest range package paths, support ant style package paths.
     *
     * example code:
     * {@code
     * // example 1:
     * // "cn.test"    is contain "cn.test.lang1", "cn.test.lang1",     so keep only "cn.test".
     * // "org.horizon.sdk.library" is contain "org.horizon.sdk.library.xxx", "org.horizon.sdk.library.ss.xx", so keep only "org.horizon.sdk.library".
     * // "cn.core"    not contain the other package path, so keep it.
     * // the output is ["cn.test", "org.horizon.sdk.library", "cn.core"]
     * Classes.getTheMostLargerRangePackagePath(List.of("cn.test.lang1", "cn.test.lang1", "cn.test", "org.horizon.sdk.library", "org.horizon.sdk.library.xxx", "org.horizon.sdk.library.ss.xx", "cn.core"));
     *
     * // example 2:
     * // there is a package path like ["cn.test.lang", "cn.test.lang.collection"].
     * // the output is ["cn.test.lang"]
     * Classes.getTheMostLargerRangePackagePath(List.of("cn.test.*", "cn.test.lang.collection"));
     *
     * // warning: this following is not the expected result:
     * // all package paths start with "org.horizon.sdk.library", and although the second package name is inconsistent, it will also be filtered out.
     * // the output is ["org.horizon.sdk.library"]
     * Classes.getTheMostLargerRangePackagePath(List.of("org.horizon.sdk.library.lang1", "org.horizon.sdk.library.lang1", "org.horizon.sdk.library", "org.horizon.sdk.library2", "org.horizon.sdk.library2.xxx", "org.horizon.sdk.library2.ss.xx", "org.horizon.sdk.library3"));
     * }
     * </pre>
     *
     * @param packagePaths the package paths
     * @return the largest range package paths
     * @see org.horizon.sdk.library.java.tool.lang.object.Classes#getTheLargestRangePackagePath(Collection)
     */
    public static Set<String> getTheLargestRangePackagePath(Collection<String> packagePaths) {
        return org.horizon.sdk.library.java.tool.lang.object.Classes.getTheLargestRangePackagePath(parseAntStylePackagePathsToPackagePaths(packagePaths));
    }

    /**
     * scan all classes containing the specified annotation in the package path of class marked {@link SpringBootApplication}.
     *
     * @param annotationClass the specified annotation class
     * @return all classes containing the specified annotation in the package path of class marked {@link SpringBootApplication}
     * @apiNote this function scans every time and has poor performance, recommended to use {@link #scanByTypeFilter(Collection, Collection)} function.
     * @see org.horizon.sdk.library.java.tool.lang.object.Classes#scanByAnnotation(Class, String...)
     */
    public static Set<Class<?>> scanByAnnotation(Class<? extends Annotation> annotationClass) {
        return Try.of(() -> org.horizon.sdk.library.java.tool.lang.object.Classes.scanByAnnotation(annotationClass, Springs.getSpringBootApplicationPackagePath())).getOrElse(Collections.newHashSet());
    }

    /**
     * scan all subclasses or implementation classes of the specified class or interface in the package path of class marked {@link SpringBootApplication}.
     *
     * @param rootClass the specified class
     * @param <T>       the specified class type
     * @return all subclasses or implementation classes of the specified class or interface in the package path of class marked {@link SpringBootApplication}
     * @apiNote this function scans every time and has poor performance, recommended to use {@link #scanByTypeFilter(Collection, Collection)} function.
     * @see org.horizon.sdk.library.java.tool.lang.object.Classes#scanBySuper(Class, String...)
     */
    public static <T> Set<Class<? extends T>> scanBySuper(Class<T> rootClass) {
        return Try.of(() -> org.horizon.sdk.library.java.tool.lang.object.Classes.scanBySuper(rootClass, Springs.getSpringBootApplicationPackagePath())).getOrElse(Collections.newHashSet());
    }

    /**
     * scan all classes in the {@link #getBasePackagePath() base package path}
     *
     * @return all classes in the {@link #getBasePackagePath() base package path}
     * @see #getBasePackagePath()
     */
    public static Set<Class<?>> scanByBasePackagePath() {
        return scanByPackagePath(getBasePackagePath());
    }

    /**
     * use default package path to scan bean definition
     *
     * @param annotationType the specified annotation to match
     * @return all {@link BeanDefinition}s by the specified {@link AnnotationTypeFilter} in default packages paths.
     * @see #scanByAnnotationTypeFilter(Class, Collection)
     * @see #getBasePackagePath()
     */
    public static Set<BeanDefinition> scanByAnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        return scanByAnnotationTypeFilter(annotationType, getBasePackagePath());
    }

    /**
     * see {@link #scanByAnnotationTypeFilter(Class, Collection)}
     *
     * @param annotationType   the specified annotation to match
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link AnnotationTypeFilter} in the specified packages paths.
     * @see #scanByAnnotationTypeFilter(Class, Collection)
     */
    public static Set<BeanDefinition> scanByAnnotationTypeFilter(Class<? extends Annotation> annotationType, String... scanPackagePaths) {
        return scanByAnnotationTypeFilter(annotationType, Collections.ofHashSet(scanPackagePaths));
    }

    /**
     * use {@link AnnotationTypeFilter} to {@link #scanByTypeFilter(TypeFilter, Collection)}
     *
     * @param annotationType   the specified annotation to match
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link AnnotationTypeFilter} in the specified packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     */
    public static Set<BeanDefinition> scanByAnnotationTypeFilter(Class<? extends Annotation> annotationType, Collection<String> scanPackagePaths) {
        return scanByTypeFilter(new AnnotationTypeFilter(annotationType), scanPackagePaths);
    }

    /**
     * use default package path to scan bean definition
     *
     * @param targetType the specified class to match
     * @return all {@link BeanDefinition}s by the specified {@link AssignableTypeFilter} in default packages paths.
     * @see #scanByAssignableTypeFilter(Class, Collection)
     * @see #getBasePackagePath()
     */
    public static Set<BeanDefinition> scanByAssignableTypeFilter(Class<?> targetType) {
        return scanByAssignableTypeFilter(targetType, getBasePackagePath());
    }

    /**
     * see {@link #scanByAssignableTypeFilter(Class, Collection)}
     *
     * @param targetType       the specified class to match
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link AssignableTypeFilter} in the specified packages paths.
     * @see #scanByAssignableTypeFilter(Class, Collection)
     */
    public static Set<BeanDefinition> scanByAssignableTypeFilter(Class<?> targetType, String... scanPackagePaths) {
        return scanByAssignableTypeFilter(targetType, Collections.ofHashSet(scanPackagePaths));
    }

    /**
     * use {@link AssignableTypeFilter} to {@link #scanByTypeFilter(TypeFilter, Collection)}
     *
     * @param targetType       the specified class to match
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link AssignableTypeFilter} in the specified packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     */
    public static Set<BeanDefinition> scanByAssignableTypeFilter(Class<?> targetType, Collection<String> scanPackagePaths) {
        return scanByTypeFilter(new AssignableTypeFilter(targetType), scanPackagePaths);
    }

    /**
     * use default package path to scan bean definition
     *
     * @param includeFilter the specified {@link TypeFilter}s
     * @return all {@link BeanDefinition}s by the specified {@link TypeFilter} in default packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     * @see #getBasePackagePath()
     */
    public static Set<BeanDefinition> scanByTypeFilter(TypeFilter includeFilter) {
        return scanByTypeFilter(Collections.ofImmutableList(includeFilter), getBasePackagePath());
    }

    /**
     * see {@link #scanByTypeFilter(Collection, Collection)}
     *
     * @param includeFilter    the specified {@link TypeFilter}
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link TypeFilter}s in the specified packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     */
    public static Set<BeanDefinition> scanByTypeFilter(TypeFilter includeFilter, String... scanPackagePaths) {
        return scanByTypeFilter(includeFilter, Collections.ofHashSet(scanPackagePaths));
    }

    /**
     * see {@link #scanByTypeFilter(Collection, Collection)}
     *
     * @param includeFilter    the specified {@link TypeFilter}
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link TypeFilter}s in the specified packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     */
    public static Set<BeanDefinition> scanByTypeFilter(TypeFilter includeFilter, Collection<String> scanPackagePaths) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(includeFilter);
        // note: all blank package paths must be filtered out, otherwise, it will globally scan all paths, causing the entire program to get stuck.
        return scanPackagePaths.stream().filter(Nil::isNotBlank).map(scanner::findCandidateComponents).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    /**
     * see {@link #scanByTypeFilter(Collection, Collection)}
     *
     * @param includeFilters   the specified {@link TypeFilter}s
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link TypeFilter}s in the specified packages paths.
     * @see #scanByTypeFilter(Collection, Collection)
     */
    public static Set<BeanDefinition> scanByTypeFilter(Collection<TypeFilter> includeFilters, String... scanPackagePaths) {
        return scanByTypeFilter(includeFilters, Collections.ofHashSet(scanPackagePaths));
    }

    /**
     * <pre>
     * scan all {@link BeanDefinition}s by the specified {@link TypeFilter}s in the specified packages paths.
     *
     * example code:
     * {@code
     *
     *     // define an interface
     *     public interface CustomerService {
     *
     *     }
     *
     *     // define the implement class1
     *     public class CustomerServiceImpl1 implements CustomerService {
     *
     *     }
     *
     *     // define the implement class2
     *     public class CustomerServiceImpl2 implements CustomerService {
     *
     *     }
     *
     *     // define the implement class3
     *     public class CustomerServiceImpl3 implements CustomerService {
     *
     *     }
     *
     *     // define enum with a customer annotation such as @CustomerAnnotation, and mark to a class like GenderType.
     *     @CustomerAnnotation
     *     public enum GenderType {
     *
     *         WOMAN,
     *         MAN,
     *         UNKNOWN,
     *
     *         ;
     *
     *     }
     *
     *     // the unit test
     *     public class Test {
     *         public static void main(String[] args) {
     *             // the output is a set {@link BeanDefinition} contains [CustomerServiceImpl1, CustomerServiceImpl2, CustomerServiceImpl3, GenderType]
     *             Springs.scanByTypeFilter(
     *                     Collections.ofArrayList(
     *                             // the annotation type filter, it will scan all objects containing the specified annotation in the specified package paths.
     *                             new AnnotationTypeFilter(CustomerAnnotation.class),
     *                             // the annotation type filter, it will scan all subclasses or implementation classes in the specified package paths.(not contains parent class)
     *                             new AssignableTypeFilter(CustomerService.class)
     *                     ),
     *                     // the specified package paths
     *                     "cn.xx1", "cn.xx2", "cn.xx3"
     *             );
     *         }
     *     }
     * }
     * </pre>
     *
     * @param includeFilters   the specified {@link TypeFilter}s
     * @param scanPackagePaths the specified package paths
     * @return all {@link BeanDefinition}s by the specified {@link TypeFilter}s in the specified packages paths.
     */
    public static Set<BeanDefinition> scanByTypeFilter(Collection<TypeFilter> includeFilters, Collection<String> scanPackagePaths) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        includeFilters.forEach(scanner::addIncludeFilter);
        // note: all blank package paths must be filtered out, otherwise, it will globally scan all paths, causing the entire program to get stuck.
        return scanPackagePaths.stream()
                .filter(Nil::isNotBlank)
                .map(scanner::findCandidateComponents)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * <pre>
     * convert class name to resource path
     *
     * for example:
     * the input is "Classes", the output is "Classes".
     * the input is "org.horizon.sdk.library.java.tool.spring.base.Classes", the output is "org/horizon/sdk/library/java/tool/spring/base/Classes".
     * </pre>
     *
     * @param className the class name
     * @return the resource path
     * @see ClassUtils#convertClassNameToResourcePath(String)
     */
    public static String convertClassNameToResourcePath(String className) {
        return ClassUtils.convertClassNameToResourcePath(className);
    }

    /**
     * <pre>
     * convert class name to class path
     *
     * for example:
     * the input is "Classes", the output is "classpath*:Classes".
     * the input is "org.horizon.sdk.library.java.tool.spring.base.Classes", the output is "classpath*:org/horizon/sdk/library/java/tool/spring/base/Classes".
     * </pre>
     *
     * @param className the class name
     * @return the class path
     */
    public static String convertClassNameToClassPath(String className) {
        return ClassConstant.CLASS_PATH_PREFIX + convertClassNameToResourcePath(className);
    }

    /**
     * <pre>
     * convert resource path to class name
     *
     * for example:
     * the input is "Classes", the output is "Classes".
     * the input is "org/horizon/sdk/library/java/tool/spring/base/Classes", the output is "org.horizon.sdk.library.java.tool.spring.base.Classes".
     * </pre>
     *
     * @param resourcePath the class name
     * @return the resource path
     * @see ClassUtils#convertResourcePathToClassName(String)
     */
    public static String convertResourcePathToClassName(String resourcePath) {
        return ClassUtils.convertResourcePathToClassName(resourcePath);
    }

    /**
     * see {@link #parseAntStylePackagePathsToPackagePaths(Collection)}
     *
     * @param antStylePackagePaths the ant style package paths
     * @return package paths
     */
    public static Set<String> parseAntStylePackagePathsToPackagePaths(String... antStylePackagePaths) {
        return parseAntStylePackagePathsToPackagePaths(Collections.ofHashSet(antStylePackagePaths));
    }

    /**
     * <pre>
     * parse ant style package paths to package paths.
     * the ant style see {@link AntPathMatcher}.
     *
     * for example:
     * the input is "cn.test.*.lang"
     * the output are ["cn.test.spring.lang", "cn.test.guava.lang", "cn.test.apache.lang"]
     * </pre>
     *
     * @param antStylePackagePaths the ant style package paths
     * @return package paths
     * @see AntPathMatcher
     * @see PathMatchingResourcePatternResolver
     */
    public static Set<String> parseAntStylePackagePathsToPackagePaths(Collection<String> antStylePackagePaths) {
        return antStylePackagePaths.stream()
                .map(Classes::convertClassNameToClassPath)
                .map(Classes::parseAntStyleClassPathsToPackagePaths)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * see {@link #parseAntStyleClassPathsToPackagePaths(Collection)}
     *
     * @param antStyleClassPaths the ant style class paths
     * @return package paths
     */
    public static Set<String> parseAntStyleClassPathsToPackagePaths(String... antStyleClassPaths) {
        return parseAntStyleClassPathsToPackagePaths(Collections.ofHashSet(antStyleClassPaths));
    }

    /**
     * <pre>
     * parse ant style class paths to package paths.
     * the ant style see {@link AntPathMatcher}.
     *
     * for example:
     * the input is "classpath*:cn/test/&#42;/lang"
     * the output are ["cn.test.spring.lang", "cn.test.guava.lang", "cn.test.apache.lang"]
     * </pre>
     *
     * @param antStyleClassPaths the ant style class paths
     * @return package paths
     * @see AntPathMatcher
     * @see PathMatchingResourcePatternResolver
     */
    public static Set<String> parseAntStyleClassPathsToPackagePaths(Collection<String> antStyleClassPaths) {
        return parseAntStyleClassPathsToResourcePaths(antStyleClassPaths)
                .stream()
                .filter(resourcePath -> Strings.notContains(resourcePath, ClassConstant.JAVA_TEST_PATH))
                .map(resourcePath -> {
                    String relativeResourcePath;
                    // parse the relative resource path:
                    if (Strings.containsAny(resourcePath, ClassConstant.JAVA_MAIN_PATH)) {
                        // if the resource path like "/absolutePath/projectName/build/classes/java/main/cn/test/lang/", then parse it to "cn/test/lang/"
                        relativeResourcePath = Strings.subAfter(resourcePath, ClassConstant.JAVA_MAIN_PATH);
                    } else if (Strings.containsAny(resourcePath, ClassConstant.RESOURCE_MAIN_PATH)) {
                        // if the resource path like "/absolutePath/projectName/build/resources/main/cn/test/lang/", then parse it to "cn/test/lang/"
                        relativeResourcePath = Strings.subAfter(resourcePath, ClassConstant.RESOURCE_MAIN_PATH);
                    } else if (Strings.containsAny(resourcePath, ClassConstant.DOCKER_APP_CLASS_PATH)) {
                        // if the resource path like "/absolutePath/app/classes/cn/test/lang/", then parse it to "cn/test/lang/"
                        relativeResourcePath = Strings.subAfter(resourcePath, ClassConstant.DOCKER_APP_CLASS_PATH);
                    } else if (Strings.containsAny(resourcePath, ClassConstant.DOCKER_APP_RESOURCE_PATH)) {
                        // if the resource path like "/absolutePath/app/resources/cn/test/lang/", then parse it to "cn/test/lang/"
                        relativeResourcePath = Strings.subAfter(resourcePath, ClassConstant.DOCKER_APP_RESOURCE_PATH);
                    } else if (Strings.startWith(resourcePath, ProtocolConstant.FILE) && Strings.containsAny(resourcePath, ClassConstant.REFERENCE_JAR_PATH)) {
                        // if the resource path like "file:/absolutePath/xxx.jar!/cn/test/lang/", then parse it to "cn/test/lang/"
                        relativeResourcePath = Strings.subAfter(resourcePath, ClassConstant.REFERENCE_JAR_PATH);
                    } else {
                        throw new LibraryJavaInternalException(Strings.format("{}parse ant style class path to package path failed because of the unsupported resource path [{}]", ModuleView.TOOL_CLASS_SYSTEM, resourcePath));
                    }
                    return Strings.removeIfEndWith(convertResourcePathToClassName(relativeResourcePath), SymbolConstant.DOT);
                })
                .collect(Collectors.toSet());
    }

    /**
     * see {@link #parseAntStyleClassPathsToResourcePaths(Collection)}
     *
     * @param antStyleClassPaths the ant style class paths
     * @return resource paths
     */
    public static Set<String> parseAntStyleClassPathsToResourcePaths(String... antStyleClassPaths) {
        return parseAntStyleClassPathsToResourcePaths(Collections.ofHashSet(antStyleClassPaths));
    }

    /**
     * <pre>
     * parse ant style class paths to resource paths.
     * the ant style see {@link AntPathMatcher}.
     *
     * for example:
     * the input is "classpath*:cn/test/&#42;/lang"
     * the output are ["/xxx/xxx/xxx/cn/test/spring/lang/", "/xxx/xxx/xxx/cn/test/guava/lang/", "/xxx/xxx/xxx/cn/test/apache/lang/"]
     * </pre>
     *
     * @param antStyleClassPaths the ant style class paths
     * @return resource paths
     * @see AntPathMatcher
     * @see PathMatchingResourcePatternResolver#getResources(String)
     * @see #getResourceByAntStyleClassPath(String)
     */
    public static Set<String> parseAntStyleClassPathsToResourcePaths(Collection<String> antStyleClassPaths) {
        return antStyleClassPaths.stream()
                .map(Classes::getResourceByAntStyleClassPath)
                .flatMap(Collection::stream)
                .map(resource -> Try.of(() -> resource.getURL().getPath()).get())
                .collect(Collectors.toSet());
    }

    /**
     * use {@link AnnotationConstant#DEFAULT_FIELD_NAME} to {@link #parseAnnotationAntStylePackagePathsToPackagePaths(Class, String)}
     *
     * @param annotationType the annotation class
     * @return package paths
     * @see #parseAnnotationAntStylePackagePathsToPackagePaths(Class, String)
     */
    public static Set<String> parseAnnotationAntStylePackagePathsToPackagePaths(Class<? extends Annotation> annotationType) {
        return parseAnnotationAntStylePackagePathsToPackagePaths(annotationType, AnnotationConstant.DEFAULT_FIELD_NAME);
    }

    /**
     * <pre>
     * parse ant style package paths specified in annotation class to package paths.
     * the ant style see {@link AntPathMatcher}.
     *
     * example code:
     * {@code
     *     // define an annotation to set the ant style package paths
     *     @Retention(RetentionPolicy.RUNTIME)
     *     @Target(ElementType.TYPE)
     *     @Documented
     *     public @interface Scanner {
     *         String[] scanPackagePaths();
     *     }
     *
     *     // define a scanner marked with @Scanner("cn.test.*.lang")
     *     @Scanner("cn.test.*.lang")
     *     public class LangPackageScanner {}
     *
     *     // define a scanner marked with @Scanner("cn.test.*.stream")
     *     @Scanner("cn.test.*.stream")
     *     public class StreamPackageScanner {}
     *
     *     // then you can use this function to collection all actual package paths.
     *     // the output example: ["cn.test.spring.lang", "cn.test.guava.lang", "cn.test.apache.lang", "cn.test.spring.stream", "cn.test.guava.stream", "cn.test.apache.stream"]
     *     Classes.parseAnnotationAntStylePackagePathToPackagePath(Scanner.class, "scanPackagePaths");
     * }
     * </pre>
     *
     * @param annotationType the annotation class
     * @param fieldName      the field name in annotation class
     * @return package paths
     * @see AntPathMatcher
     * @see PathMatchingResourcePatternResolver
     * @see #parseAntStylePackagePathsToPackagePaths(Collection)
     */
    public static Set<String> parseAnnotationAntStylePackagePathsToPackagePaths(Class<? extends Annotation> annotationType, String fieldName) {
        return parseAntStylePackagePathsToPackagePaths(Annotations.getAnnotationNestValues(annotationType, String[].class, fieldName));
    }

    /**
     * use {@link AnnotationConstant#DEFAULT_FIELD_NAME} to {@link #optimizeAnnotationAntStylePackagePaths(Class, String)}
     *
     * @param annotationType the annotation class
     * @return package paths
     * @see #optimizeAnnotationAntStylePackagePaths(Class, String)
     */
    public static Set<String> optimizeAnnotationAntStylePackagePaths(Class<? extends Annotation> annotationType) {
        return optimizeAnnotationAntStylePackagePaths(annotationType, AnnotationConstant.DEFAULT_FIELD_NAME);
    }

    /**
     * <pre>
     * use {@link #parseAnnotationAntStylePackagePathsToPackagePaths(Class, String)} to parse ant style package paths specified in annotation class,
     * then {@link #getTheLargestRangePackagePath(Collection)} by the package paths specified in annotation class and {@link Springs#getSpringBootApplicationPackagePath()}.
     * </pre>
     *
     * @param annotationType the annotation class
     * @param fieldName      the field name in annotation class
     * @return the optimal package paths
     * @see #parseAnnotationAntStylePackagePathsToPackagePaths(Class, String)
     * @see #getTheLargestRangePackagePath(Collection)
     * @see Springs#getSpringBootApplicationPackagePath()
     */
    public static Set<String> optimizeAnnotationAntStylePackagePaths(Class<? extends Annotation> annotationType, String fieldName) {
        return getTheLargestRangePackagePath(Collections.add(
                parseAnnotationAntStylePackagePathsToPackagePaths(annotationType, fieldName),
                Springs.getSpringBootApplicationPackagePath()
        ));
    }

}