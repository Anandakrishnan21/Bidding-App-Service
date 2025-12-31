package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieveAllAuctionQuery implements Query<List<AuctionResponse>> {
}
