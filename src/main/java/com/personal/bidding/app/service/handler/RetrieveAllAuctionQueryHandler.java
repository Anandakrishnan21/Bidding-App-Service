package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.query.RetrieveAllAuctionQuery;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetrieveAllAuctionQueryHandler implements QueryHandler<RetrieveAllAuctionQuery, List<AuctionResponse>> {
    private final AuctionRepository auctionRepository;

    @Override
    public List<AuctionResponse> handle(RetrieveAllAuctionQuery query) {
        List<AuctionResponse> auctionResponseList = new ArrayList<>();
        List<AuctionEntity> auctionEntities = auctionRepository.findAll();

        try {
            if (!auctionEntities.isEmpty()) {
                for (AuctionEntity auctionEntity : auctionEntities) {

                    AuctionResponse auctionResponse = AuctionResponse.builder()
                            .auctionName(auctionEntity.getAuctionName())
                            .increaseRate(auctionEntity.getIncreaseRate())
                            .sportsType(auctionEntity.getSportsType())
                            .teamPoints(auctionEntity.getTeamPoints())
                            .minimumBid(auctionEntity.getMinimumBid())
                            .maxPlayers(auctionEntity.getMaxPlayers())
                            .createdAt(auctionEntity.getCreatedAt())
                            .updatedAt(auctionEntity.getUpdatedAt())
                            .build();

                    auctionResponseList.add(auctionResponse);
                }

                log.info("Auctions retrieved from the DB are :: {}", auctionResponseList);
                return auctionResponseList;
            } else {
                throw new AuctionNotFoundException("No auctions exist in the DB");
            }
        } catch (AuctionNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO RETRIEVE AUCTIONS :: ", exe.getCause());
        }
    }
}
