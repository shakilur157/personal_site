package com.sazzadur.repositories;

import com.sazzadur.models.UserRoles;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<UserRoles, String> {

}
