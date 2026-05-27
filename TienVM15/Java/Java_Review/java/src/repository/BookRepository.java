package repository;

import model.Book;
import java.util.List;

public interface BookRepository {
    boolean add(Book book);
    List<Book> getAll();
    boolean update(Book book);
    boolean delete(String id);
    Book findById(String id);
}
