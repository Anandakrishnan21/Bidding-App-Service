package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.PlayerNotFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.PlayerEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.UpdatePlayerQuery;
import com.personal.bidding.app.model.response.PlayerResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.repository.PlayerRepository;
import com.personal.bidding.app.service.utils.Helper;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdatePlayerQueryHandler implements QueryHandler<UpdatePlayerQuery, PlayerResponse> {
    private final PlayerRepository playerRepository;
    private final AuctionRepository auctionRepository;
    private final Helper helper;

    @Override
    @Transactional
    public PlayerResponse handle(UpdatePlayerQuery query) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByAuctionNameAndPlayerId(query.getAuctionName(), query.getPlayerId());

        PlayerResponse playerResponse = new PlayerResponse();
        try {
            if (playerEntity.isPresent()) {
                updateAuction(query);

                PlayerEntity player = playerEntity.get();
                player.setPlayerName(query.getPlayerRequest().getPlayerName());
                player.setPlayerRole(query.getPlayerRequest().getPlayerRole());
                player.setCreatedAt(LocalDateTime.now());

                playerRepository.save(player);

                log.info("Player with ID :: {} is successfully updated", query.getPlayerId());
                helper.copyProperties(player, playerResponse);
                return playerResponse;
            } else {
                throw new PlayerNotFoundException("Player with ID :: " + query.getPlayerId() + " is not present in the auction with ID :: " + query.getPlayerId());
            }
        } catch (PlayerNotFoundException bxe) {
            throw bxe;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO UPDATE THE PLAYER :: ", exe.getCause());
        }
    }

    private void updateAuction(UpdatePlayerQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionName(query.getAuctionName());

        if (auctionEntity.isPresent()) {
            AuctionEntity auction = auctionEntity.get();
            boolean isPlayerUpdated = false;

            if (!ObjectUtils.isEmpty(auction.getTeamDetails())) {
                for (TeamDetails teams : auction.getTeamDetails()) {
                    if (!ObjectUtils.isEmpty(teams.getPlayerDetails())) {
                        isPlayerUpdated = teams.getPlayerDetails().stream()
                                .filter(player -> player.getPlayerId().equalsIgnoreCase(query.getPlayerId()))
                                .findFirst()
                                .map(player -> {
                                    if (!ObjectUtils.isEmpty(player.getPlayerName())
                                            && !ObjectUtils.isEmpty(player.getPlayerRole())) {
                                        player.setPlayerName(query.getPlayerRequest().getPlayerName());
                                        player.setPlayerRole(query.getPlayerRequest().getPlayerRole());
                                        return true;
                                    } else {
                                        return false;
                                    }
                                }).orElse(false);
                    }
                }
            }

            if (isPlayerUpdated) {
                auction.setUpdatedAt(LocalDateTime.now());
                auctionRepository.save(auction);
            } else {
                log.info("Failed to update the auction due to some error");
            }
        } else {
            log.info("Auction :: {} is not present in the DB", query.getAuctionName());
        }
    }
}
