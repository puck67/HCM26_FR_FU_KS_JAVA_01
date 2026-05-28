package fa.training.dao;

import fa.training.entities.CinemaRoomDetail;

import java.util.List;

public interface CinemaRoomDetailDAO {

    void insert(CinemaRoomDetail detail);

    CinemaRoomDetail getById(int id);

    List<CinemaRoomDetail> getAll();

    void update(CinemaRoomDetail detail);

    void delete(int id);
}