package com.mejner.projectmanagertool.repositories;

import com.mejner.projectmanagertool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {



}
