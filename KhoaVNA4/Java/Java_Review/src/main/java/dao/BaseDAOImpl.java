package dao;

import common.ResultSetMapper;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseDAOImpl<T> implements BaseDAO<T> {
    protected final Class<T> entityClass;
    protected final String tableName;
    protected final ResultSetMapper<T> defaultMapper;

    @SuppressWarnings("unchecked")
    protected BaseDAOImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];

        String className = entityClass.getSimpleName();
        if ("Order".equalsIgnoreCase(className)) {
            this.tableName = "Orders";
        } else {
            this.tableName = className;
        }

        this.defaultMapper = this::mapResultSetToEntity;
    }

    protected String toColumnName(String fieldName) {
        return fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    protected T mapResultSetToEntity(ResultSet rs) throws SQLException {
        throw new UnsupportedOperationException("mapResultSetToEntity must be implemented by subclass");
    }

    @Override
    public boolean add(T entity) {
        throw new UnsupportedOperationException("save must be implemented by subclass using stored procedures");
    }

    @Override
    public boolean update(T entity) {
        throw new UnsupportedOperationException("update must be implemented by subclass using stored procedures");
    }

    @Override
    public boolean delete(Object id) {
        throw new UnsupportedOperationException("delete must be implemented by subclass using stored procedures");
    }

    @Override
    public T findById(Object id) {
        throw new UnsupportedOperationException("findById must be implemented by subclass using stored procedures");
    }

    @Override
    public List<T> findAll() {
        throw new UnsupportedOperationException("findAll must be implemented by subclass using stored procedures");
    }
}
