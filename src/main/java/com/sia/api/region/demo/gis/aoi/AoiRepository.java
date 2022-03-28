package com.sia.api.region.demo.gis.aoi;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 관심지역 repository
 */
public interface AoiRepository extends JpaRepository<Aoi, Integer> {
}
