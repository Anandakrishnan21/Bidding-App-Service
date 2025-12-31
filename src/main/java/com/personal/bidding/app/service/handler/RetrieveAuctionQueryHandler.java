package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.query.RetrieveAuctionQuery;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetrieveAuctionQueryHandler implements QueryHandler<RetrieveAuctionQuery, AuctionResponse> {
    private final AuctionRepository auctionRepository;

    @Override
    public AuctionResponse handle(RetrieveAuctionQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        try {
            if (auctionEntity.isPresent()) {
                AuctionEntity auction = auctionEntity.get();

                AuctionResponse auctionResponse = AuctionResponse.builder()
                        .auctionName(auction.getAuctionName())
                        .increaseRate(auction.getIncreaseRate())
                        .sportsType(auction.getSportsType())
                        .teamPoints(auction.getTeamPoints())
                        .minimumBid(auction.getMinimumBid())
                        .maxPlayers(auction.getMaxPlayers())
                        .build();

                log.info("Auction retrieved from the DB :: {}", auctionResponse);
                return auctionResponse;
            } else {
                throw new AuctionNotFoundException("Auction with id :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (Exception e) {
            throw new BusinessException("FAILED TO RETRIEVE AUCTION :: ", e.getCause());
        }
    }
}
