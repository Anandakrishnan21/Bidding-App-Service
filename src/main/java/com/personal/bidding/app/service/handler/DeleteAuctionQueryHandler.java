package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.query.DeleteAuctionQuery;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteAuctionQueryHandler implements QueryHandler<DeleteAuctionQuery, String> {
    private final AuctionRepository auctionRepository;

    @Override
    public String handle(DeleteAuctionQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        try {
            if (auctionEntity.isPresent()) {
                auctionRepository.delete(auctionEntity.get());
                log.info("Auction with id :: {} deleted successfully", query.getAuctionId());

                return "Auction with id :: " + query.getAuctionId() + " deleted successfully";
            } else {
                throw new AuctionNotFoundException("Auction with auction id :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO DELETE THE AUCTION :: ", exe.getCause());
        }
    }
}
