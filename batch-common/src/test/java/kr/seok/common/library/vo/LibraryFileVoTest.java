package kr.seok.common.library.vo;

import kr.seok.common.entity.EntityField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

class LibraryFileVoTest {

    LibraryFileVo libVo;
    List<LibraryFileVo> libs;

    @BeforeEach
    public void setUp() {
        libs = new ArrayList<>();
        libVo = new LibraryFileVo();
        libVo.setLibraryNm("도서관 명");
        libVo.setCityNm("도시 명");
        libVo.setCountryNm("읍면동 명");
        libVo.setLibraryType("도서관 타입");
        libs.add(libVo);
        libVo = new LibraryFileVo();
        libVo.setLibraryNm("도서관 명1");
        libVo.setCityNm("도시 명1");
        libVo.setCountryNm("읍면동 명1");
        libVo.setLibraryType("도서관 타입1");
        libs.add(libVo);
    }

    @Test
    void testCase1() {

        Map<String, String> nameMap = new HashMap<>();
        Map<String, String> columnMap = new HashMap<>();
        List<String> fieldNames = new ArrayList<>();

        for(LibraryFileVo item : libs) {
            List<Field> libFields = getAllFields(item.getClass());
            for(Field field : libFields) {
                fieldNames.add(field.getName());
                if(field.isAnnotationPresent(EntityField.class)) {
                    EntityField annotation = field.getAnnotation(EntityField.class);
                    nameMap.put(field.getName(), annotation.fieldName());
                    columnMap.put(field.getName(), annotation.columnName());
                }
            }
        }
        for(Map.Entry<String, String> entry : nameMap.entrySet()) {
            System.out.println(entry);
        }
        for(Map.Entry<String, String> entry : columnMap.entrySet()) {
            System.out.println(entry);
        }
    }

    @Test
    void testCase2() {
        for(LibraryFileVo item : libs) {
            List<String> allFieldNames = getAllFieldNames(item.getClass());
            for(String field : allFieldNames) {
                System.out.println(field);
            }
        }
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields;
    }

    public static List<String> getAnnotations(
            Class<?> clazz, Class<? extends Annotation> targetAnnotation
    ) {
        List<String> annotations = new ArrayList<>();
        for(Field item : getAllFields(clazz)) {
            if(item.isAnnotationPresent(targetAnnotation)) {
                EntityField annotation = (EntityField) item.getAnnotation(targetAnnotation);
                annotations.add(annotation.columnName());
            }
        }
        return annotations;
    }

    public static List<String> getAllFieldNames(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields.stream().map(Field::getName).collect(Collectors.toList());
    }

    private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (fromSuper) {
            Collections.reverse(classes);
        }
        return classes;
    }
}
