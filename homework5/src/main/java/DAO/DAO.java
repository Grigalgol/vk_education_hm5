package dao;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    @NotNull T get(int id) throws SQLException;

    @NotNull List< @NotNull T> all();

    void save(@NotNull T entity);

    void update(@NotNull T entity);

    void delete(@NotNull T entity);
}
