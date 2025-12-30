package com.personal.bidding.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerResponse {
    private String playerName;
    private String auctionName;
    private String playerRole;
    private double basePrice;
    private double soldPrice;
    private String playerStatus;
    private String teamName;
}
