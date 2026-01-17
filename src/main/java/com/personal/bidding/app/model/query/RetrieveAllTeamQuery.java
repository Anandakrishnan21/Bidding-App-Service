package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.service.utils.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieveAllTeamQuery implements Query<List<TeamResponse>> {
    private String auctionId;
    private String teamName;
    private Pageable pageable;
}
