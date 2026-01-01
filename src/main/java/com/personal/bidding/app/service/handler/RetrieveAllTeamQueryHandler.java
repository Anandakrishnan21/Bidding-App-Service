package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.RetrieveAllTeamQuery;
import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.repository.AuctionRepository;
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
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RetrieveAllTeamQueryHandler implements QueryHandler<RetrieveAllTeamQuery, List<TeamResponse>> {
    private final AuctionRepository auctionRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<TeamResponse> handle(RetrieveAllTeamQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        List<TeamResponse> teamResponseList = new ArrayList<>();

        try {
            if (auctionEntity.isPresent()) {
                List<TeamDetails> teamDetails = auctionEntity.get().getTeamDetails();

                if (
                        Objects.nonNull(teamDetails)
                                && !teamDetails.isEmpty()
                ) {
                    if (!StringUtils.isEmpty(query.getTeamName())) {
                        teamDetails = teamSearchFilter(query);
                    }

                    for (TeamDetails team : teamDetails) {
                        TeamResponse teamResponse = TeamResponse.builder()
                                .teamName(team.getTeamName())
                                .teamLogo(team.getTeamLogo())
                                .remainingAmount(team.getRemainingAmount())
                                .build();
                        teamResponseList.add(teamResponse);
                    }
                }

                log.info("All team details :: {}", teamResponseList);
                return teamResponseList;
            } else {
                throw new AuctionNotFoundException("Auction with id :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO RETRIEVE TEAM DETAILS :: ", exe.getCause());
        }
    }

    private List<TeamDetails> teamSearchFilter(RetrieveAllTeamQuery query) {
        Query mongoQuery = new Query();
        mongoQuery.addCriteria(
                Criteria.where("auctionId").is(query.getAuctionId())
                        .and("teamDetails.teamName").is(query.getTeamName())
        );

        List<AuctionEntity> auctionEntities = mongoTemplate.find(mongoQuery, AuctionEntity.class);

        return auctionEntities.stream()
                .flatMap(auction -> auction.getTeamDetails().stream())
                .filter(team -> team.getTeamName().equals(query.getTeamName()))
                .toList();
    }
}
