package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.TokensEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends CrudRepository<TokensEntity, String> {
}
