package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.request.TeamRequest;
import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateTeamQuery implements Query<TeamResponse> {
    private String auctionId;
    private String teamName;
    private TeamRequest teamRequest;
}
