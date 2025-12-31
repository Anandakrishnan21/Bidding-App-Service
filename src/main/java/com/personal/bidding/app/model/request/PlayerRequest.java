package com.personal.bidding.app.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRequest {
    private String playerName;
    private String auctionName;
    private String playerRole;
    private double basePrice;
}
