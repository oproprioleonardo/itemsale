package com.leonardo.minecraft.itemsale.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Boost {

    private Double multiplier;
    private Long time;

}
