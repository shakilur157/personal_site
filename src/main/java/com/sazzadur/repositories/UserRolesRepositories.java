package com.sazzadur.repositories;

import com.sazzadur.models.UserRoles;
import com.sazzadur.enums.UserType;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRolesRepositories extends CrudRepository<UserRoles, Long> {

    Optional<UserRoles> findByUserType(UserType role);
}
