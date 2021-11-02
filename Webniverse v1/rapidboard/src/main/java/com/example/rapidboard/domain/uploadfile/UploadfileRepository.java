package com.example.rapidboard.domain.uploadfile;

import com.example.rapidboard.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadfileRepository extends JpaRepository<Uploadfile, Long> {
        List<Uploadfile> findAllByPostAndIsDeleted(Post post, int is_deleted);
}
