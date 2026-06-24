package com.example.demo.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenericTableHelper {
    public static Class<?> getEntityClass(Object item) {
        if (item == null) {
            return null;
        }
        Class<?> clazz = item.getClass();
        while (clazz != null && (clazz.getName().contains("$$") || clazz.getName().contains("HibernateProxy"))) {
            clazz = clazz.getSuperclass();
        }
        return clazz;
    }

    public static List<String> getHeaders(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        Field[] fields = clazz.getDeclaredFields();
        List<String> headers = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getName().equalsIgnoreCase("id")) {
                headers.add(toTitleCase(field.getName()));
            }
        }
        return headers;
    }

    public static List<Map<String, Object>> getRows(List<?> list) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        Class<?> clazz = getEntityClass(list.get(0));
        Field[] fields = clazz.getDeclaredFields();

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Object item : list) {
            if (item == null) {
                continue;
            }
            Map<String, Object> row = new LinkedHashMap<>();
            Object idVal = null;
            for (Field field : fields) {
                Object val = getFieldValue(item, field);
                if (field.getName().equalsIgnoreCase("id")) {
                    idVal = val;
                } else {
                    row.put(field.getName(), val != null ? val : "");
                }
            }
            row.put("rowId", idVal != null ? idVal : "");
            rows.add(row);
        }
        return rows;
    }

    public static List<FieldDescriptor> getFields(Object entity) {
        if (entity == null) {
            return Collections.emptyList();
        }
        Class<?> clazz = getEntityClass(entity);
        Field[] fields = clazz.getDeclaredFields();
        List<FieldDescriptor> descriptors = new ArrayList<>();
        for (Field field : fields) {
            if (field.getName().equalsIgnoreCase("id")) {
                continue;
            }
            String name = field.getName();
            String label = toTitleCase(name);
            String type = "text";
            if (field.getType() == Double.class || field.getType() == double.class ||
                field.getType() == Integer.class || field.getType() == int.class ||
                field.getType() == Long.class || field.getType() == long.class) {
                type = "number";
            } else if (name.toLowerCase().contains("email")) {
                type = "email";
            }
            Object value = getFieldValue(entity, field);
            descriptors.add(new FieldDescriptor(name, label, type, value));
        }
        return descriptors;
    }

    private static Object getFieldValue(Object item, Field field) {
        try {
            return new org.springframework.beans.BeanWrapperImpl(item).getPropertyValue(field.getName());
        } catch (Exception e) {
            try {
                field.setAccessible(true);
                return field.get(item);
            } catch (Exception ex) {
                return null;
            }
        }
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c) || c == '_' || c == '-') {
                nextTitleCase = true;
                sb.append(' ');
            } else if (nextTitleCase) {
                sb.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
