package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.PlayerNotFoundException;
import com.personal.bidding.app.model.entity.PlayerEntity;
import com.personal.bidding.app.model.query.RetrievePlayersQuery;
import com.personal.bidding.app.model.response.PlayerResponse;
import com.personal.bidding.app.service.utils.Helper;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetrievePlayersQueryHandler implements QueryHandler<RetrievePlayersQuery, List<PlayerResponse>> {
    private final MongoTemplate mongoTemplate;
    private final Helper helper;

    @Override
    public List<PlayerResponse> handle(RetrievePlayersQuery query) {
        List<PlayerEntity> playerEntities = searchFilter(query);
        List<PlayerResponse> playerResponseList = new ArrayList<>();
        PlayerResponse playerResponse = new PlayerResponse();

        try {
            if (!playerEntities.isEmpty()) {

                for (PlayerEntity player : playerEntities) {
                    helper.copyProperties(player, playerResponse);
                    playerResponseList.add(playerResponse);
                }
                log.info("Players available for the auction with ID :: {} are Players :: {}", query.getAuctionId(), playerResponseList);
                return playerResponseList;
            } else {
                throw new PlayerNotFoundException("No players are available in the auction with ID :: " + query.getAuctionId() + " with the respective query");
            }
        } catch (PlayerNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("PLAYER RETRIEVAL FAILED :: ", exe.getCause());
        }
    }

    private List<PlayerEntity> searchFilter(RetrievePlayersQuery query) {
        Query mongoQuery = new Query();
        mongoQuery.addCriteria(Criteria.where("auctionId").is(query.getAuctionId()));

        if (!StringUtils.isEmpty(query.getPlayerName())) {
            mongoQuery.addCriteria(Criteria.where("playerName").is(query.getPlayerName()));
        }
        if (!StringUtils.isEmpty(query.getPlayerRole())) {
            mongoQuery.addCriteria(Criteria.where("playerRole").is(query.getPlayerRole()));
        }
        if (!StringUtils.isEmpty(query.getPlayerStatus())) {
            mongoQuery.addCriteria(Criteria.where("playerStatus").is(query.getPlayerStatus()));
        }
        return mongoTemplate.find(mongoQuery, PlayerEntity.class);
    }
}
