package fa.training.dao;

import fa.training.entities.Seat;

public class SeatDao extends AbstractDao<Seat, Integer> {
    public SeatDao() {
        super(Seat.class);
    }
}
