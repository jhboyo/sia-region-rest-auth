package com.sia.api.region.demo.gis.region;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 행정지역 repository
 */
public interface RegionRepository extends JpaRepository<Region, Integer> {
}
