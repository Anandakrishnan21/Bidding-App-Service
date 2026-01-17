package com.personal.bidding.app.controller;

import com.personal.bidding.app.model.query.CreateTeamQuery;
import com.personal.bidding.app.model.query.DeleteTeamQuery;
import com.personal.bidding.app.model.query.RetrieveAllTeamQuery;
import com.personal.bidding.app.model.query.UpdateTeamQuery;
import com.personal.bidding.app.model.request.TeamRequest;
import com.personal.bidding.app.model.response.TeamResponse;
import com.personal.bidding.app.service.utils.QueryExecutor;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {
    private final QueryExecutor queryExecutor;

    public TeamController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @ApiOperation(value = "Create a new team")
    @PostMapping("/{auctionId}/team")
    public TeamResponse createTeam(@PathVariable String auctionId,
                                   @RequestBody TeamRequest teamRequest) {
        CreateTeamQuery request = CreateTeamQuery.builder()
                .auctionId(auctionId)
                .teamRequest(teamRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation(value = "Retrieve teams based on search query")
    @GetMapping("/{auctionId}/team")
    public List<TeamResponse> retrieveAllTeam(@PathVariable String auctionId,
                                              @RequestParam(required = false) String teamName,
                                              @RequestParam int page,
                                              @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        RetrieveAllTeamQuery request = RetrieveAllTeamQuery.builder()
                .auctionId(auctionId)
                .teamName(teamName)
                .pageable(pageable)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation(value = "Update team by team name from an auction")
    @PutMapping("/{auctionId}/team")
    public TeamResponse updateTeam(@PathVariable String auctionId,
                                   @RequestParam String teamName,
                                   @RequestBody TeamRequest teamRequest) {
        UpdateTeamQuery request = UpdateTeamQuery.builder()
                .auctionId(auctionId)
                .teamName(teamName)
                .teamRequest(teamRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation(value = "Delete team by team name from an auction")
    @DeleteMapping("/{auctionId}/team")
    public String deleteTeam(@PathVariable String auctionId,
                             @RequestParam String teamName) {
        DeleteTeamQuery request = DeleteTeamQuery.builder()
                .auctionId(auctionId)
                .teamName(teamName)
                .build();
        return queryExecutor.execute(request);
    }
}
