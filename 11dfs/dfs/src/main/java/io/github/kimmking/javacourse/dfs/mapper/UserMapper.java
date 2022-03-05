package io.github.kimmking.javacourse.dfs.mapper;

import io.github.kimmking.javacourse.dfs.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int create(User user);
    User findById(Long id);
}
