package com.personal.bidding.app.repository;

import com.personal.bidding.app.model.entity.PlayerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends MongoRepository<PlayerEntity, String> {

    Optional<PlayerEntity> findByPlayerNameAndAuctionName(String playerName, String auctionName);

    Optional<PlayerEntity> findByPlayerId(String playerId);

    Optional<PlayerEntity> findByAuctionIdAndPlayerId(String auctionId, String playerId);

}
