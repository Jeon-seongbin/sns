package com.project.sns.repository;

import com.project.sns.model.entity.LikeEntity;
import com.project.sns.model.entity.PostEntity;
import com.project.sns.model.entity.UserEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity userEntity, PostEntity postEntity);

    List<LikeEntity> findAllByPost(PostEntity post);

    long countByPost(PostEntity post);

    @Transactional
//    @Query("Update LikeEntity entity SET removed_at = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}