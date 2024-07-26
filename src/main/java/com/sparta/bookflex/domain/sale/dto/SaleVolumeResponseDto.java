package com.sparta.bookflex.domain.sale.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaleVolumeResponseDto {

    private List<SaleVolumeRowDto> SaleVolumeRowList;

    private TotalSaleVolumeDto inquiryTotalSaleVolume;

    @Builder
    public SaleVolumeResponseDto(List<SaleVolumeRowDto> firstData, TotalSaleVolumeDto secondData) {
        this.SaleVolumeRowList = firstData;
        this.inquiryTotalSaleVolume = secondData;
    }

}
