package com.tripcok.tripcokserver.domain.group.entity;


import com.tripcok.tripcokserver.domain.place.entity.Place;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class GroupPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 모임 장소 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place; // 장소

    @Setter
    private Integer orders; // 순서

    public void updateOrder(Integer order) {
        this.orders = order;
    }

    public GroupPlace(Group group, Place place, Integer order) {
        this.group = group;
        this.place = place;
        this.orders = order;
    }

    public GroupPlace() {

    }
}
