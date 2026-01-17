package com.personal.bidding.app.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionResponse {
    private String auctionName;
    private String sportsType;
    private Integer teamPoints;
    private Integer minimumBid;
    private Integer increaseRate;
    private Integer maxPlayers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
