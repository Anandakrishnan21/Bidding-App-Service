package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.response.BiddingResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerBiddingQuery implements Query<BiddingResponse> {
    private String auctionId;
    private String auctionName;
    private String teamName;
    private String playerId;
    private double soldPrice;
}
