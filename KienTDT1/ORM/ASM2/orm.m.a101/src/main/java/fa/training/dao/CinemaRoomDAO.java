package fa.training.dao;

import fa.training.entities.CinemaRoom;

import java.util.List;

public interface CinemaRoomDAO {

    void insert(CinemaRoom room);

    CinemaRoom getById(int id);

    List<CinemaRoom> getAll();

    void update(CinemaRoom room);

    void delete(int id);
}