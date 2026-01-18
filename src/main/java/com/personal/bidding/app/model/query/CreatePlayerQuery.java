package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.request.PlayerRequest;
import com.personal.bidding.app.model.response.PlayerResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlayerQuery implements Query<PlayerResponse> {
    private String auctionId;
    private PlayerRequest playerRequest;
}
