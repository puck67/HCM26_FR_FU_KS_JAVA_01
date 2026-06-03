package fa.training.dao;

import fa.training.entities.Seat;

import java.util.List;

public interface SeatDAO {

    void insert(Seat seat);

    Seat getById(int id);

    List<Seat> getAll();

    void update(Seat seat);

    void delete(int id);
}