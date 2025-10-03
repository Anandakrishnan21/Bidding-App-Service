package com.personal.bidding.app.service;

import com.personal.bidding.app.exception.AuctionExistsException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.request.AuctionRequest;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionResponse createAuction(AuctionRequest auctionRequest) {
        Optional<AuctionEntity> isAuctionExists = auctionRepository.findByAuctionName(auctionRequest.getAuctionName());
        AuctionResponse auctionResponse = new AuctionResponse();
        try {

            if (isAuctionExists.isEmpty()) {
                AuctionEntity auctionEntity = AuctionEntity.builder()
                        .auctionName(auctionRequest.getAuctionName())
                        .sportsType(auctionRequest.getSportsType())
                        .minimumBid(auctionRequest.getMinimumBid())
                        .increaseRate(auctionRequest.getIncreaseRate())
                        .maxPlayers(auctionRequest.getMaxPlayers())
                        .teamPoints(auctionRequest.getTeamPoints())
                        .build();

                auctionRepository.save(auctionEntity);
                log.info("Auction with id {} added to the DB", auctionEntity.getAuctionId());

                BeanUtils.copyProperties(auctionEntity, auctionResponse);
                return auctionResponse;

            } else {
                throw new AuctionExistsException("Auction with same data already exist in the DB");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
