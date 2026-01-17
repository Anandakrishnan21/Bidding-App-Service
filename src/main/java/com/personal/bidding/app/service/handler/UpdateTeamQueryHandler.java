package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.TeamNotFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.UpdateTeamQuery;
import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.Helper;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateTeamQueryHandler implements QueryHandler<UpdateTeamQuery, TeamResponse> {
    private final AuctionRepository auctionRepository;
    private final Helper helper;

    @Override
    public TeamResponse handle(UpdateTeamQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        TeamResponse teamResponse = new TeamResponse();
        try {
            if (auctionEntity.isPresent()) {
                AuctionEntity auction = auctionEntity.get();
                List<TeamDetails> teamDetailsList = auction.getTeamDetails();

                if (!ObjectUtils.isEmpty(teamDetailsList)) {
                    for (TeamDetails team : teamDetailsList) {
                        if (
                                StringUtils.hasText(team.getTeamName())
                                        && team.getTeamName().equalsIgnoreCase(query.getTeamName())
                        ) {
                            team.setTeamName(query.getTeamRequest().getTeamName());
                            team.setTeamLogo(query.getTeamRequest().getTeamLogo());

                            helper.copyProperties(team, teamResponse);
                        }
                    }

                    auction.setUpdatedAt(LocalDateTime.now());
                    auctionRepository.save(auction);

                    log.info("Team details updated for the team :: {}", query.getTeamName());
                    return teamResponse;
                } else {
                    throw new TeamNotFoundException("Team with name :: " + query.getTeamRequest().getTeamName() + " is not present the DB");
                }
            } else {
                throw new AuctionNotFoundException("Auction with auction ID :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException | TeamNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO UPDATE THE TEAM :: ", exe.getCause());
        }
    }
}
