package com.personal.bidding.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamDetails {
    private String teamLogo;
    private String teamName;
    private double remainingAmount;
    private List<PlayerDetails> playerDetails;
}
