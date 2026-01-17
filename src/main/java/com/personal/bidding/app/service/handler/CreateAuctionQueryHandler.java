package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionExistsException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.query.CreateAuctionQuery;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.Helper;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateAuctionQueryHandler implements QueryHandler<CreateAuctionQuery, AuctionResponse> {
    private final AuctionRepository auctionRepository;
    private final Helper helper;

    @Override
    public AuctionResponse handle(CreateAuctionQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionNameAndSportsType(query.getAuctionName(), query.getSportsType());
        AuctionResponse auctionResponse = new AuctionResponse();
        try {
            if (auctionEntity.isPresent()) {
                log.info("Auction with same auction name and sports type exist in the DB ::");
                throw new AuctionExistsException("Auction with same auction name and sports type exist in the DB");
            }

            AuctionEntity auction = AuctionEntity.builder()
                    .auctionId(UUID.randomUUID().toString())
                    .auctionName(query.getAuctionName())
                    .sportsType(query.getSportsType())
                    .minimumBid(query.getMinimumBid())
                    .increaseRate(query.getIncreaseRate())
                    .maxPlayers(query.getMaxPlayers())
                    .teamPoints(query.getTeamPoints())
                    .createdAt(LocalDateTime.now())
                    .build();

            auctionRepository.save(auction);

            log.info("Auction with id :: {} added to the DB", auction.getId());
            helper.copyProperties(auction, auctionResponse);
            log.info("Auction Response :: {}", auctionResponse);
            return auctionResponse;

        } catch (AuctionExistsException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO ADD NEW AUCTION :: ", exe.getCause());
        }
    }
}
