package com.example.rapidboard.domain.webinar;

import com.example.rapidboard.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebinarRepository extends JpaRepository<Webinar, Long> {
    List<Webinar> findAllByIsDeleted(int isDeleted);
    Webinar findWebinarByIsMain(int isMain);
    Page<Webinar> findAllByIsDeletedOrderByStartDateAsc(int isDeleted, Pageable pageable);
}
