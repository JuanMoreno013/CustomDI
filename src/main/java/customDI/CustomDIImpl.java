package customDI;

import customDI.annotations.CustomBean;
import customDI.annotations.CustomInject;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class CustomDIImpl {

    private final Map<String, Object> beanDependents = new HashMap<>();
    private final Map<String, Object> allBeans = new HashMap<>();


    public CustomDIImpl() {
        Set<Class<?>> allClasses = getClasses();
        Set<Class<?>> beanClasses = getBeanClasses(allClasses);
        Set<Class<?>> classDependent = getClassDependents(allClasses);

        createInstancesBean(beanClasses);
        injectBeansOnClasses(classDependent);

    }


    private Set<Class<?>> getClasses() {

//        Set<?> classes;

//        List<ClassLoader> classLoadersList = new LinkedList<>();
//        classLoadersList.add(ClasspathHelper.contextClassLoader());
//        classLoadersList.add(ClasspathHelper.staticClassLoader());

//        Reflections reflections = new Reflections(new ConfigurationBuilder()
//                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
//                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
//                .filterInputsBy(new FilterBuilder().includePackage("customDI")), new SubTypesScanner(false));


//        Reflections reflections = new Reflections("customDI");

//        classes =  new Reflections("customDI");
//        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages("customDI"), new SubTypesScanner(false));

        Reflections reflections = new Reflections("customDI", new SubTypesScanner(false));
        return reflections.getSubTypesOf(Object.class);

//        return reflections.getSubTypesOf();

//        return classes;
    }

    private Set<Class<?>> getBeanClasses(Set<Class<?>> classes) {

        return classes.stream()
                .filter(clas -> clas.isAnnotationPresent(CustomBean.class))
                .collect(Collectors.toSet());
    }


    private Set<Class<?>> getClassDependents(Set<Class<?>> classes) {
        return classes.stream()
                .filter(aClass -> Arrays.stream(aClass.getDeclaredFields())
                        .anyMatch(field -> field.isAnnotationPresent(CustomInject.class)))

                .collect(Collectors.toSet());
    }


    private void createInstancesBean(Set<Class<?>> beansClass) {
        beansClass.forEach(beanClazz -> {
            try {
                allBeans.put(beanClazz.getSimpleName(), beanClazz.getConstructor().newInstance());
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException |
                     IllegalAccessException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private void injectBeansOnClasses(Set<Class<?>> classes) {

        classes.forEach(classDependents -> {

            try {
                Object obj = classDependents.getConstructor().newInstance();

                Arrays.stream(classDependents.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(CustomInject.class))
                        .forEach(field -> {
                            field.setAccessible(true);

                            try {
                                field.set(obj, allBeans.get(field.getType().getSimpleName()));

                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

    }


}
