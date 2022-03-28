package com.sia.api.region.demo.gis.aoi;

import com.sia.api.region.demo.common.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AoiControllerTests extends BaseTest {

    @Autowired
    AoiRepository aoiRepository;


    @Test
    @DisplayName("행정지역에 지리적으로 포함되는 관심지역을 조회")
    public void getAoi() throws Exception {
        //Given
        int regionId = 1;

        // When & Then
        this.mockMvc.perform(get("/regions/{id}/aois/intersects", regionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("area").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-aoi"))
        ;
    }

    @Test
    @DisplayName("행정지역에 지리적으로 포함되지 않는 관심지역을 조회")
    public void getAoi_notfound() throws Exception {
        //Given
        int regionId = 1212120012;

        // When & Then
        this.mockMvc.perform(get("/regions/{id}/aois/intersects", regionId))
                .andExpect(status().isNotFound())
        ;
    }





    @Disabled
    @Test
    @DisplayName("관심지역 이벤트를 정상적으로 생성하는 테스트")
    public void createAoi() throws Exception {

        AoiResponseDto aoiDto = AoiResponseDto.builder()
                            .name("의왕시")
                             .build();

    }
}
