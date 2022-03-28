package com.sia.api.region.demo.gis.region;

import lombok.*;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 행정지역 Entity
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Getter
@Setter
@Entity
public class Region {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String name;

    /**
     * 아래 jts Geometry Type이 Database 의 Geometry Polygon Type과 Correct Matching인지 확인 필요
     */
    @Column(columnDefinition = "geometry(Polygon, 4326)")
    private Geometry area;

}
