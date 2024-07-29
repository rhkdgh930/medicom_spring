package com.team5.hospital_here.board.repository;

import com.team5.hospital_here.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    //List<Post> findByBoardId(Long boardId);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByBoardId(Long boardId, Pageable pageable);
    List<Post> findByTitleContaining(String title);
}
