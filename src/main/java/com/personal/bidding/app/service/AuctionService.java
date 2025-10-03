package com.personal.bidding.app.service;

import com.personal.bidding.app.exception.AuctionExistsException;
import com.personal.bidding.app.exception.NoDataFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.request.AuctionRequest;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuctionService {
    private final AuctionRepository auctionRepository;

    public AuctionResponse createAuction(AuctionRequest auctionRequest) {
        Optional<AuctionEntity> auction = auctionRepository.findByAuctionName(auctionRequest.getAuctionName());
        AuctionResponse auctionResponse = new AuctionResponse();
        try {
            if (auction.isEmpty()) {

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

    public List<AuctionResponse> retrieveAllAuction() {
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
                            .build();

                    auctionResponseList.add(auctionResponse);
                }

                log.info("Auctions retrieved from the DB: {}", auctionResponseList);
                return auctionResponseList;
            } else {
                throw new NoDataFoundException("No data exists in the DB");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuctionResponse retrieveAuction(String auctionName) {
        Optional<AuctionEntity> auction = auctionRepository.findByAuctionName(auctionName);
        try {
            if (auction.isPresent()) {
                AuctionEntity auctionEntity = auction.get();

                AuctionResponse auctionResponse = AuctionResponse.builder()
                        .auctionName(auctionEntity.getAuctionName())
                        .increaseRate(auctionEntity.getIncreaseRate())
                        .sportsType(auctionEntity.getSportsType())
                        .teamPoints(auctionEntity.getTeamPoints())
                        .minimumBid(auctionEntity.getMinimumBid())
                        .maxPlayers(auctionEntity.getMaxPlayers())
                        .build();

                log.info("Auction retrieved from the DB: {}", auctionResponse);
                return auctionResponse;
            } else {
                throw new NoDataFoundException("Auction with " + auctionName + " not found in the DB");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuctionResponse updateAuction(String auctionName, AuctionRequest auctionRequest) {
        Optional<AuctionEntity> auction = auctionRepository.findByAuctionName(auctionName);
        AuctionResponse auctionResponse = new AuctionResponse();
        try {
            if (auction.isPresent()) {
                AuctionEntity auctionEntity = auction.get();

                auctionEntity.setIncreaseRate(auctionRequest.getIncreaseRate());
                auctionEntity.setSportsType(auctionRequest.getSportsType());
                auctionEntity.setTeamPoints(auctionRequest.getTeamPoints());
                auctionEntity.setMinimumBid(auctionRequest.getMinimumBid());
                auctionEntity.setMaxPlayers(auctionRequest.getMaxPlayers());

                log.info("Updated auction details: {}", auctionEntity);
                auctionRepository.save(auctionEntity);

                BeanUtils.copyProperties(auctionEntity, auctionResponse);
                return auctionResponse;
            } else {
                throw new NoDataFoundException("Auction with " + auctionName + " not found in the DB");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String deleteAuction(String auctionName) {
        Optional<AuctionEntity> auction = auctionRepository.findByAuctionName(auctionName);
        try {
            if (auction.isPresent()) {

                auctionRepository.delete(auction.get());
                log.info("Auction with auction name {} deleted successfully", auctionName);

                return "Auction with auction name " + auctionName + " deleted successfully";
            } else {
                throw new NoDataFoundException("Auction with " + auctionName + " not found in the DB");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
