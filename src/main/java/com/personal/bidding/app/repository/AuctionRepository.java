package com.personal.bidding.app.repository;

import com.personal.bidding.app.model.entity.AuctionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuctionRepository extends MongoRepository<AuctionEntity, String> {
   Optional<AuctionEntity> findByAuctionName(String auctionName);
}
