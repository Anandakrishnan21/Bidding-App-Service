package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.EmptyFieldException;
import com.personal.bidding.app.exception.NoDataFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.query.UpdateAuctionQuery;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateAuctionQueryHandler implements QueryHandler<UpdateAuctionQuery, AuctionResponse> {
    private final AuctionRepository auctionRepository;

    @Override
    public AuctionResponse handle(UpdateAuctionQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        AuctionResponse auctionResponse = new AuctionResponse();

        try {
            if (auctionEntity.isPresent()) {
                AuctionEntity auction = auctionEntity.get();

                if (
                        Objects.nonNull(query.getAuctionRequest().getTeamPoints())
                                && !StringUtils.isEmpty(query.getAuctionRequest().getAuctionName())
                                && !StringUtils.isEmpty(query.getAuctionRequest().getSportsType())
                                && Objects.nonNull(query.getAuctionRequest().getMinimumBid())
                                && Objects.nonNull(query.getAuctionRequest().getIncreaseRate())
                                && Objects.nonNull(query.getAuctionRequest().getMaxPlayers())
                ) {

                    auction.setAuctionName(query.getAuctionRequest().getAuctionName());
                    auction.setIncreaseRate(query.getAuctionRequest().getIncreaseRate());
                    auction.setSportsType(query.getAuctionRequest().getSportsType());
                    auction.setTeamPoints(query.getAuctionRequest().getTeamPoints());
                    auction.setMinimumBid(query.getAuctionRequest().getMinimumBid());
                    auction.setMaxPlayers(query.getAuctionRequest().getMaxPlayers());

                    log.info("Updated auction details for the auction :: {} with data:: {}", auction.getAuctionName(), auction);
                    auctionRepository.save(auction);

                    BeanUtils.copyProperties(auctionEntity, auctionResponse);
                    return auctionResponse;
                } else {
                    throw new EmptyFieldException("Few fields are appeared to be empty");
                }
            } else {
                throw new AuctionNotFoundException("Auction with id :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException | EmptyFieldException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO UPDATE THE AUCTION :: ", exe.getCause());
        }
    }
}
