package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.PlayerExistException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.PlayerEntity;
import com.personal.bidding.app.model.query.CreatePlayerQuery;
import com.personal.bidding.app.model.response.PlayerResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.repository.PlayerRepository;
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
public class CreatePlayerQueryHandler implements QueryHandler<CreatePlayerQuery, PlayerResponse> {
    private final PlayerRepository playerRepository;
    private final AuctionRepository auctionRepository;
    private final Helper helper;

    @Override
    public PlayerResponse handle(CreatePlayerQuery query) {
        Optional<PlayerEntity> isPlayerExisted = playerRepository.findByPlayerNameAndAuctionName(query.getPlayerRequest().getPlayerName(), query.getAuctionName());
        Optional<AuctionEntity> isAuctionExist = auctionRepository.findByAuctionName(query.getAuctionName());

        PlayerResponse playerResponse = new PlayerResponse();

        try {
            if (isAuctionExist.isPresent()) {
                if (isPlayerExisted.isPresent()) {
                    throw new PlayerExistException("Player exist in the DB, please create a new player");
                }

                PlayerEntity playerEntity = PlayerEntity.builder()
                        .playerName(query.getPlayerRequest().getPlayerName())
                        .playerRole(query.getPlayerRequest().getPlayerRole())
                        .playerId(UUID.randomUUID().toString())
                        .auctionName(query.getAuctionName())
                        .basePrice(query.getPlayerRequest().getBasePrice())
                        .soldPrice(0)
                        .playerStatus("AVAILABLE")
                        .createdAt(LocalDateTime.now())
                        .build();

                playerRepository.save(playerEntity);
                log.info("Player saved to the DB successfully");

                helper.copyProperties(playerEntity, playerResponse);
                return playerResponse;
            } else {
                throw new AuctionNotFoundException("Auction with name :: " + query.getAuctionName() + " is not present in the DB");
            }

        } catch (AuctionNotFoundException | PlayerExistException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO CREATE NEW PLAYER :: ", exe.getCause());
        }
    }
}
