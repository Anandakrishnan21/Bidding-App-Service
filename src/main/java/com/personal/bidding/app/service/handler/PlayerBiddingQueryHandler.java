package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.PlayerExistException;
import com.personal.bidding.app.exception.PlayerNotFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.PlayerDetails;
import com.personal.bidding.app.model.entity.PlayerEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.PlayerBiddingQuery;
import com.personal.bidding.app.model.response.BiddingResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.repository.PlayerRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerBiddingQueryHandler implements QueryHandler<PlayerBiddingQuery, BiddingResponse> {
    private final AuctionRepository auctionRepository;
    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public BiddingResponse handle(PlayerBiddingQuery query) {
        Optional<AuctionEntity> existedAuction = auctionRepository.findByAuctionId(query.getAuctionId());
        Optional<PlayerEntity> playerEntity = playerRepository.findByPlayerId(query.getPlayerId());

        try {
            if (existedAuction.isPresent()) {

                if (
                        playerEntity.isPresent()
                        && playerEntity.get().getAuctionName().equalsIgnoreCase(query.getAuctionName())
                        && (Objects.isNull(playerEntity.get().getPlayerStatus()))
                ) {

                    PlayerDetails playerDetails = retreivePlayerDetails(playerEntity.get(), query.getSoldPrice());

                    AuctionEntity auctionEntity = existedAuction.get();
                    auctionEntity.getTeamDetails().stream()
                            .filter(teamDetails -> Objects.equals(teamDetails.getTeamName(), query.getTeamName())
                            ).forEach(teamDetails -> {
                                if (Objects.isNull(teamDetails.getPlayerDetails())) {
                                    teamDetails.setPlayerDetails(new ArrayList<>());
                                }

                                filterPlayers(teamDetails, query.getPlayerId());

                                if (
                                        teamDetails.getPlayerDetails().size() < auctionEntity.getMaxPlayers()
                                        && teamDetails.getRemainingAmount() >= query.getSoldPrice()
                                ) {
                                    teamDetails.setRemainingAmount(teamDetails.getRemainingAmount() - query.getSoldPrice());
                                    teamDetails.getPlayerDetails().add(playerDetails);
                                }
                            });

                    auctionEntity.setUpdatedAt(LocalDateTime.now());
                    auctionRepository.save(auctionEntity);
                    log.info("BIDDING SUCCESSFUL FOR PLAYER {}", playerDetails.getPlayerName());
                    updatePlayerEntity(playerEntity.get(), query.getSoldPrice(), query.getTeamName());

                    return BiddingResponse.builder()
                            .playerName(playerDetails.getPlayerName())
                            .teamName(query.getTeamName())
                            .playerRole(playerDetails.getPlayerRole())
                            .basePrice(playerDetails.getBasePrice())
                            .soldPrice(query.getSoldPrice())
                            .build();
                } else {
                    throw new PlayerNotFoundException("Player with id " + query.getPlayerId() + " does not exist in the DB or already sold");
                }
            } else {
                throw new AuctionNotFoundException("Auction with name " + query.getAuctionName() + " does not exist in the DB");
            }
        } catch (PlayerNotFoundException | AuctionNotFoundException | PlayerExistException bex) {
            log.info("Exception :: {}", bex.getMessage());
            throw bex;
        } catch (Exception exe) {
            log.info("Bidding Failed :: {}", exe.getMessage());
            throw new BusinessException("BIDDING FAILED :: ", exe.getCause());
        }
    }

    private void updatePlayerEntity(PlayerEntity playerEntity, double soldPrice, String teamName) {
        playerEntity.setSoldPrice(soldPrice);
        playerEntity.setPlayerStatus("SOLD");
        playerEntity.setTeamName(teamName);
        playerEntity.setUpdatedAt(LocalDateTime.now());
        playerRepository.save(playerEntity);
    }

    private void filterPlayers(TeamDetails teamDetails, String playerId) {
        boolean playerNotFound = teamDetails.getPlayerDetails().stream()
                .noneMatch(playerDetails -> playerDetails.getPlayerId().equalsIgnoreCase(playerId));
        if (!playerNotFound) {
            throw new PlayerExistException("Player with id " + playerId + " already bid in the auction");
        }
    }

    private PlayerDetails retreivePlayerDetails(PlayerEntity playerEntity, double soldPrice) {
        return PlayerDetails.builder()
                .playerId(playerEntity.getPlayerId())
                .playerName(playerEntity.getPlayerName())
                .playerRole(playerEntity.getPlayerRole())
                .basePrice(playerEntity.getBasePrice())
                .soldPrice(soldPrice)
                .createdAt(playerEntity.getCreatedAt())
                .createdAt(playerEntity.getUpdatedAt())
                .build();
    }
}
