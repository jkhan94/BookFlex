package com.sparta.bookflex.domain.sale.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class TotalSaleVolumeDto {

    private BigDecimal totalSaleVolume;

    @Builder
    public TotalSaleVolumeDto(BigDecimal totalSaleVolume) {
        this.totalSaleVolume = totalSaleVolume;
    }

}
