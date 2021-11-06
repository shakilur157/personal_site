package com.sazzadur.repositories;

import com.sazzadur.models.UserPosts;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends CrudRepository<UserPosts, Long> {

    Optional<UserPosts> findUserPostsByStatus(String pending);
    List<Optional<UserPosts>> findAllByStatus(String status);
}
