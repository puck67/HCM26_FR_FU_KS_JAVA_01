package com.example.ASM5.service.base;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericFileServiceImpl<E> implements GenericFileService<E> {

    @Override
    public void saveAll(List<E> itemList, String path) {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outStream.writeObject(itemList);
            System.out.println("[GenericFileService] Saved "
                    + itemList.size() + " item(s) → " + path);
        } catch (IOException ex) {
            System.err.println("[GenericFileService] Save error: " + ex.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<E> getAll(String path) {
        List<E> dataList = new ArrayList<>();
        try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(path))) {
            dataList = (List<E>) inStream.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("[GenericFileService] Read error: " + ex.getMessage());
        }
        return dataList;
    }
}