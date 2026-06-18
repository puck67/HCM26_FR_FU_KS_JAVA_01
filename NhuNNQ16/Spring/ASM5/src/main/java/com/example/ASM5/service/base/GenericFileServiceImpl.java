package com.example.ASM5.service.base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericFileServiceImpl<T> implements GenericFileService<T> {

    @Override
    public void saveAll(List<T> items, String fileName) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(items);
            System.out.println("[GenericFileService] Saved "
                    + items.size() + " item(s) → " + fileName);
        } catch (IOException e) {
            System.err.println("[GenericFileService] Save error: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll(String fileName) {
        List<T> result = new ArrayList<>();
        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(fileName))) {
            result = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[GenericFileService] Read error: " + e.getMessage());
        }
        return result;
    }
}