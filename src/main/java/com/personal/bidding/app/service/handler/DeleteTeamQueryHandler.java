package com.personal.bidding.app.service.handler;

import com.personal.bidding.app.exception.AuctionNotFoundException;
import com.personal.bidding.app.exception.BusinessException;
import com.personal.bidding.app.exception.TeamNotFoundException;
import com.personal.bidding.app.model.entity.AuctionEntity;
import com.personal.bidding.app.model.entity.TeamDetails;
import com.personal.bidding.app.model.query.DeleteTeamQuery;
import com.personal.bidding.app.repository.AuctionRepository;
import com.personal.bidding.app.service.utils.QueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeleteTeamQueryHandler implements QueryHandler<DeleteTeamQuery, String> {
    private final AuctionRepository auctionRepository;

    @Override
    public String handle(DeleteTeamQuery query) {
        Optional<AuctionEntity> auctionEntity = auctionRepository.findByAuctionId(query.getAuctionId());
        try {
            if (auctionEntity.isPresent()) {
                List<TeamDetails> teamDetails = auctionEntity.get().getTeamDetails();

                if (
                        Objects.nonNull(teamDetails)
                                && !teamDetails.isEmpty()
                ) {

                    teamDetails.removeIf(team -> team.getTeamName().equalsIgnoreCase(query.getTeamName()));
                    return "Team with name :: " + query.getTeamName() + " deleted from the auction";
                } else {
                    throw new TeamNotFoundException("Team with name " + query.getTeamName() + " not found in the DB");
                }
            } else {
                throw new AuctionNotFoundException("Auction with auction id: " + query.getAuctionId() + " not found in the DB");
            }
        } catch (AuctionNotFoundException | TeamNotFoundException bex) {
            throw bex;
        } catch (Exception exe) {
            throw new BusinessException("FAILED TO DELETE THE TEAM :: ", exe.getCause());
        }
    }
}
