package com.example.rapidboard.domain.post;

import com.example.rapidboard.domain.board.Board;
import com.example.rapidboard.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
//    @Query(value = "SELECT * FROM post WHERE is_deleted=false ORDER BY post_id DESC", nativeQuery = true)
//    Page<Post> getPosts(Pageable pageable);
    Page<Post> findAllByIsDeletedOrderByPostIdDesc(int isDeleted, Pageable pageable);

//    @Query(value = "SELECT * FROM post WHERE board_id=:boardId AND is_deleted=false ORDER BY post_id DESC", nativeQuery = true)
//    Page<Post> getBoardPosts(Long boardId, Pageable pageable);
    Page<Post> findAllByBoardAndIsDeletedOrderByPostIdDesc(Board board, int isDeleted, Pageable pageable);

//    @Query(value = "SELECT * FROM post WHERE member_id = :memberId AND is_deleted=false ORDER BY post_id DESC", nativeQuery = true)
//    Page<Post> getMemberPosts(Long memberId, Pageable pageable);
    Page<Post> findAllByMemberAndIsDeletedOrderByPostIdDesc(Member member, int isDeleted, Pageable pageable);

    //Title + Content
    @Query(value = "SELECT * " +
            "FROM ((SELECT p.* FROM post p JOIN postcontent pc ON p.postcontent_id = pc.postcontent_id WHERE MATCH(pc.postcontent) against (:keyword IN BOOLEAN MODE) AND p.is_deleted=false) " +
            "union (SELECT * FROM post WHERE MATCH (title) against (:keyword IN BOOLEAN MODE))) pp " +
            "ORDER BY pp.create_date DESC, pp.post_id DESC", nativeQuery = true)
    Page<Post> getSearchResultTC(String keyword, Pageable pageable);

    //Title
    @Query(value = "SELECT * " +
            "FROM post " +
            "WHERE MATCH (title) against (:keyword IN BOOLEAN MODE) AND is_deleted=false " +
            "ORDER BY create_date DESC, post_id DESC", nativeQuery = true)
    Page<Post> getSearchResultT(String keyword, Pageable pageable);

    //Content
    @Query(value = "SELECT p.* " +
            "FROM post p " +
            "JOIN postcontent pc ON p.postcontent_id = pc.postcontent_id " +
            "WHERE MATCH(pc.postcontent) against (:keyword IN BOOLEAN MODE) AND p.is_deleted=false " +
            "ORDER BY create_date DESC, post_id DESC", nativeQuery = true)
    Page<Post> getSearchResultC(String keyword, Pageable pageable);

    //Member
    @Query(value = "SELECT post.* FROM post JOIN (SELECT member_id,username FROM member) m ON post.member_id = m.member_id WHERE m.username LIKE %:keyword% ORDER BY create_date DESC, post_id DESC", nativeQuery = true)
    Page<Post> getSearchResultM(String keyword, Pageable pageable);
}
