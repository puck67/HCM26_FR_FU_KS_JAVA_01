package fa.training.ex4.services.base;

import java.util.List;

public interface GenericService<ID, REQ_C, REQ_U, RES> {
    List<RES> findAll();

    RES findById(ID id);

    RES create(REQ_C request);

    RES update(ID id, REQ_U request);

    void delete(ID id);
}
