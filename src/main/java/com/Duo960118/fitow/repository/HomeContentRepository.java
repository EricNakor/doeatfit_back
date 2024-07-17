package com.Duo960118.fitow.repository;

import com.Duo960118.fitow.entity.HomeContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HomeContentRepository extends JpaRepository<HomeContentEntity,Long> {
    Optional<HomeContentEntity> findByUuidEntityUuid(UUID uuid);

    void deleteByUuidEntityUuid(UUID uuid);

    List<HomeContentEntity> findByCategory(HomeContentEntity.HomeContentCategoryEnum category);

    List<HomeContentEntity> findByIsBeingUsedTrue();
}
