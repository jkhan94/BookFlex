package com.sparta.bookflex.domain.sale.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaleVolumeResponseDto {

    private Page<SaleVolumeRowDto> SaleVolumeRowList;

    private TotalSaleVolumeDto inquiryTotalSaleVolume;

    @Builder
    public SaleVolumeResponseDto(Page<SaleVolumeRowDto> firstData, TotalSaleVolumeDto secondData) {
        this.SaleVolumeRowList = firstData;
        this.inquiryTotalSaleVolume = secondData;
    }

}
