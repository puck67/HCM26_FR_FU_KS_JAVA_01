package dao;

import entities.Cat;

import java.util.List;

public interface CatDAO  extends BaseDAO<Cat>{
    List<Cat> getAllCat();
}

