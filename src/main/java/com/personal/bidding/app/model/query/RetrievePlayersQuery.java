package com.personal.bidding.app.model.query;

import com.personal.bidding.app.model.response.PlayerResponse;
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
public class RetrievePlayersQuery implements Query<List<PlayerResponse>> {
    private String auctionName;
    private String playerRole;
    private String playerName;
    private String playerStatus;
    private String teamName;
    private Pageable pageable;
}
