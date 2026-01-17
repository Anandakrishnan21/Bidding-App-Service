package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.TeamNameExistsException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.CreateTeamQuery;
import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.Helper;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateTeamQueryHandler implements QueryHandler<CreateTeamQuery, TeamResponse> {
    private final AuctionRepository auctionRepository;
    private final Helper helper;

    @Override
    public TeamResponse handle(CreateTeamQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        TeamResponse teamResponse = new TeamResponse();

        try {
            if (auctionEntity.isPresent()) {
                AuctionEntity auction = auctionEntity.get();

                boolean isTeamNameExist = Objects.nonNull(auction.getTeamDetails())
                        && !auction.getTeamDetails().isEmpty()
                        && auction.getTeamDetails().stream()
                        .anyMatch(teamDetails ->
                                query.getTeamRequest().getTeamName().equalsIgnoreCase(teamDetails.getTeamName()));

                if (!isTeamNameExist) {
                    TeamDetails teamDetails = TeamDetails.builder()
                            .teamLogo(query.getTeamRequest().getTeamLogo())
                            .teamName(query.getTeamRequest().getTeamName())
                            .remainingAmount(auction.getTeamPoints())
                            .build();

                    List<TeamDetails> teamDetailsList = auction.getTeamDetails();
                    if (Objects.isNull(teamDetailsList)) {
                        teamDetailsList = new ArrayList<>();
                    }

                    teamDetailsList.add(teamDetails);
                    auction.setTeamDetails(teamDetailsList);

                    auction.setUpdatedAt(LocalDateTime.now());
                    auctionRepository.save(auction);
                    log.info("New team added to the auction DB with name :: {}", query.getTeamRequest().getTeamName());

                    helper.copyProperties(teamDetails, teamResponse);
                    return teamResponse;
                } else {
                    throw new TeamNameExistsException("Team with name :: " + query.getTeamRequest().getTeamName() + " already exists in the DB");
                }
            } else {
                throw new AuctionNotFoundException("Auction with ID :: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException | TeamNameExistsException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO CREATE TEAM :: ", exe.getCause());
        }
    }
}
