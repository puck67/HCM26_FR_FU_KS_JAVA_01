package com.example.demo.service;

import com.example.demo.util.ValidationUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorageHelper {

    private FileStorageHelper() {
    }

    public static <T extends Serializable> void saveList(String filename, List<T> list) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }
        for (T item : list) {
            if (item == null) {
                throw new IllegalArgumentException("List cannot contain null elements");
            }
            ValidationUtils.validate(item);
        }
        File file = new File(filename);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException(new StringBuilder("Error saving list to file: ")
                    .append(filename)
                    .toString(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> getList(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<T> list = (List<T>) obj;
                for (T item : list) {
                    if (item == null) {
                        throw new IllegalArgumentException("Loaded list contains null elements");
                    }
                    ValidationUtils.validate(item);
                }
                return list;
            }
            return new ArrayList<>();
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}
