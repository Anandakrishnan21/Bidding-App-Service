package com.personal.bidding.app.model.query;

import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeletePlayerQuery implements Query<String> {
    private String auctionId;
    private String playerId;
}
