package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.PlayerNotFoundException;
import com.personal.bidding.app.exception.PlayerSoldException;
import com.personal.bidding.app.model.entity.PlayerEntity;
import com.personal.bidding.app.model.query.DeletePlayerQuery;
import com.personal.bidding.app.repository.PlayerRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeletePlayerQueryHandler implements QueryHandler<DeletePlayerQuery, String> {
    private final PlayerRepository playerRepository;

    @Override
    public String handle(DeletePlayerQuery query) {
        Optional<PlayerEntity> playerEntity = playerRepository.findByAuctionNameAndPlayerId(query.getAuctionName(), query.getPlayerId());
        try {
            if (playerEntity.isPresent()) {
                PlayerEntity player = playerEntity.get();

                if (player.getPlayerStatus().equalsIgnoreCase("SOLD")
                        || player.getSoldPrice() > 0.0
                        || !StringUtils.isEmpty(player.getTeamName())) {
                    throw new PlayerSoldException("Player with id " + query.getPlayerId() + " is already sold in the auction");
                }
                playerRepository.delete(player);
                log.info("Player with id :: {} is successfully deleted from the auction :: {}", query.getPlayerId(), query.getAuctionName());
                return "Player with id: " + query.getPlayerId() + " is successfully deleted from the auction: " + query.getAuctionName();
            } else {
                throw new PlayerNotFoundException("Player with id: " + query.getPlayerId() + " is not present in the auction: " + query.getAuctionName());
            }
        } catch (PlayerSoldException | PlayerNotFoundException bxe) {
            throw bxe;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO DELETE THE PLAYER :: ", exe.getCause());
        }
    }
}
