package com.personal.bidding.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiddingResponse {
    private String playerName;
    private String teamName;
    private String playerRole;
    private double basePrice;
    private double soldPrice;
}
