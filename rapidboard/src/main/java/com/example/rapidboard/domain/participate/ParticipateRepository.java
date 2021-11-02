package com.example.rapidboard.domain.participate;

import com.example.rapidboard.domain.webinar.Webinar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    Participate findByMemberIdAndWebinar(Long memberId, Webinar webinar);
}
