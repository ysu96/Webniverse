package com.example.rapidboard.domain.comment;

import com.example.rapidboard.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query(value = "SELECT * FROM comment WHERE post_id = :postId ORDER BY comment_group ASC, comment_order ASC", nativeQuery = true)
//    Page<Comment> getComments(Long postId, Pageable pageable);
// -> JPA 사용
    Page<Comment> findAllByPostOrderByCommentGroupAscCommentOrderAsc(Post post, Pageable pageable);

    @Query(value = "SELECT comment_group FROM comment WHERE post_id=:postId ORDER BY comment_group DESC LIMIT 1", nativeQuery = true)
    int getLastGroup(Long postId);

    @Modifying
    @Query(value = "UPDATE comment set comment_order = comment_order + 1 WHERE post_id=:postId and comment_group=:parent_comment_group and comment_order > :current_comment_order", nativeQuery = true)
    void reorder(int parent_comment_group, int current_comment_order, Long postId);
}
