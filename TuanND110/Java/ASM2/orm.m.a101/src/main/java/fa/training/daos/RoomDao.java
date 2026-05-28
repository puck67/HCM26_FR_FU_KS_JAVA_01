package fa.training.daos;

import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Objects;

/**
 * RoomDao - Data Access Object for CinemaRoom entity (RoomDao)
 */
public class RoomDao {

    /**
     * Insert a new cinema room
     */
    public Integer insertRoom(CinemaRoom room) {
        Objects.requireNonNull(room, "room must not be null");
        Objects.requireNonNull(room.getRoomDetail(), "room.roomDetail must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Integer roomId = null;
        try {
            transaction = session.beginTransaction();
            roomId = (Integer) session.save(room);
            transaction.commit();
            System.out.println("Cinema Room inserted successfully with ID: " + roomId);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error inserting cinema room: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return roomId;
    }

    /**
     * Get cinema room by ID
     */
    public CinemaRoom getRoomById(Integer roomId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CinemaRoom room = null;
        try {
            room = session.get(CinemaRoom.class, roomId);
        } catch (Exception e) {
            System.err.println("Error retrieving cinema room: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return room;
    }

    /**
     * Get all cinema rooms
     */
    public List<CinemaRoom> getAllRooms() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<CinemaRoom> rooms = null;
        try {
            Query<CinemaRoom> query = session.createQuery("FROM CinemaRoom", CinemaRoom.class);
            rooms = query.list();
        } catch (Exception e) {
            System.err.println("Error retrieving all cinema rooms: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rooms;
    }

    /**
     * Get cinema room by name
     */
    public CinemaRoom getRoomByName(String roomName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CinemaRoom room = null;
        try {
            Query<CinemaRoom> query = session.createQuery("FROM CinemaRoom WHERE cinemaRoomName = :roomName", CinemaRoom.class);
            query.setParameter("roomName", roomName);
            room = query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error retrieving cinema room by name: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return room;
    }

    /**
     * Update cinema room
     */
    public void updateRoomById(Integer roomId, CinemaRoom roomDetails) {
        Objects.requireNonNull(roomId, "roomId must not be null");
        Objects.requireNonNull(roomDetails, "roomDetails must not be null");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, roomId);
            if (room != null) {
                room.setCinemaRoomName(roomDetails.getCinemaRoomName());
                room.setSeatQuantity(roomDetails.getSeatQuantity());
                if (roomDetails.getRoomDetail() != null) {
                    CinemaRoomDetail existingDetail = room.getRoomDetail();
                    CinemaRoomDetail newDetail = roomDetails.getRoomDetail();
                    if (existingDetail != null) {
                        existingDetail.setRoomRate(newDetail.getRoomRate());
                        existingDetail.setActiveDate(newDetail.getActiveDate());
                        existingDetail.setRoomDescription(newDetail.getRoomDescription());
                    } else {
                        room.setRoomDetail(newDetail);
                    }
                }
                session.update(room);
                transaction.commit();
                System.out.println("Cinema Room updated successfully with ID: " + roomId);
            } else {
                System.out.println("Cinema Room not found with ID: " + roomId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating cinema room: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Delete cinema room by ID
     */
    public void deleteRoomById(Integer roomId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            CinemaRoom room = session.get(CinemaRoom.class, roomId);
            if (room != null) {
                session.delete(room);
                transaction.commit();
                System.out.println("Cinema Room deleted successfully with ID: " + roomId);
            } else {
                System.out.println("Cinema Room not found with ID: " + roomId);
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting cinema room: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Count total cinema rooms
     */
    public Long countAllRooms() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Long count = 0L;
        try {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM CinemaRoom", Long.class);
            count = query.getSingleResult();
        } catch (Exception e) {
            System.err.println("Error counting cinema rooms: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return count;
    }

    /**
     * Get cinema room with details eagerly loaded
     */
    public CinemaRoom getRoomWithDetails(Integer roomId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        CinemaRoom room = null;
        try {
            Query<CinemaRoom> query = session.createQuery(
                    "FROM CinemaRoom cr LEFT JOIN FETCH cr.roomDetail LEFT JOIN FETCH cr.seats WHERE cr.cinemaRoomId = :roomId",
                    CinemaRoom.class
            );
            query.setParameter("roomId", roomId);
            room = query.uniqueResult();
        } catch (Exception e) {
            System.err.println("Error retrieving cinema room with details: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        return room;
    }
}
