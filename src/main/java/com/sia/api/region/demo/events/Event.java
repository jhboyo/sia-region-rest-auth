package com.sia.api.region.demo.events;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sia.api.region.demo.accounts.Account;
import com.sia.api.region.demo.accounts.AccountSerializer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter @Setter
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private LocalDateTime beginEnrollmentDateTime;

    private LocalDateTime closeEnrollmentDateTime;

    private LocalDateTime beginEventDateTime;

    private LocalDateTime endEventDateTime;

    private String location; // (optional) 이게 없으면 온라인 모임

    private int basePrice; // (optional)

    private int maxPrice; // (optional)

    private int limitOfEnrollment;

    private boolean offline;

    private boolean free;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

//    @JsonSerialize(using = AccountSerializer.class)
//    @ManyToOne
//    private Account manager;


    public  void update() {
        if (this.basePrice == 0 && this.maxPrice ==0) {
            this.free = true;
        } else {
            this.free = false;
        }

        // update offline
        if (this.location == null || this.location.isBlank()) {
            this.offline = false;
        } else {
            this.offline = true;
        }

    }
}