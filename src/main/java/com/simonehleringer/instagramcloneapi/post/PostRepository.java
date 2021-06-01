package com.simonehleringer.instagramcloneapi.post;

import com.simonehleringer.instagramcloneapi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserOrderByCreationTimeDesc(User user);

    List<Post> findByUserInOrderByCreationTimeDesc(List<User> users);
}
