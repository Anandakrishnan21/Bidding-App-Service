package com.personal.bidding.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionEntity {
    @Id
    private String id;
    private String auctionId;
    private String auctionName;
    private String sportsType;
    private Integer teamPoints;
    private Integer minimumBid;
    private Integer increaseRate;
    private Integer maxPlayers;
    private List<TeamDetails> teamDetails;
}
