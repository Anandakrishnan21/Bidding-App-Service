package com.personal.bidding.app.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "playerDetails")
public class PlayerEntity {
    @Id
    private String id;
    private String playerId;
    private String playerName;
    private String playerRole;
    private String auctionName;
    private double basePrice;
    private double soldPrice;
    private String playerStatus;
    private String teamName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
