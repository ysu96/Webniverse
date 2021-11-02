package com.example.rapidboard.domain.participate;


import com.example.rapidboard.domain.webinar.Webinar;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Participate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participate_id")
    private Long participateId;

    @Column(nullable = false, name = "member_id")
    private Long memberId;

    @JsonIgnore
    @JoinColumn(name = "webinar_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Webinar webinar;

}
