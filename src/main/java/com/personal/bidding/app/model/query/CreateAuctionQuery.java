package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAuctionQuery implements Query<AuctionResponse> {
    private String auctionName;
    private String sportsType;
    private Integer teamPoints;
    private Integer minimumBid;
    private Integer increaseRate;
    private Integer maxPlayers;
}
