package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.BlackListedToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlackListRepository extends CrudRepository<BlackListedToken, String> {
}
