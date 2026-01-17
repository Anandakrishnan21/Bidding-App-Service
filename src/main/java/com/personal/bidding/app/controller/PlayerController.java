package com.personal.bidding.app.controller;

import com.personal.bidding.app.model.query.CreatePlayerQuery;
import com.personal.bidding.app.model.query.DeletePlayerQuery;
import com.personal.bidding.app.model.query.RetrievePlayersQuery;
import com.personal.bidding.app.model.query.UpdatePlayerQuery;
import com.personal.bidding.app.model.request.PlayerRequest;
import com.personal.bidding.app.model.response.PlayerResponse;
import com.personal.bidding.app.service.utils.QueryExecutor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    private final QueryExecutor queryExecutor;

    @Autowired
    public PlayerController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @ApiOperation("Create new player by auction id")
    @PostMapping("/{auctionName}/players")
    public PlayerResponse createPlayer(@PathVariable String auctionName,
                                       @RequestBody PlayerRequest playerRequest) {
        CreatePlayerQuery request = CreatePlayerQuery.builder()
                .auctionName(auctionName)
                .playerRequest(playerRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Retrieve players based on the query")
    @GetMapping("/{auctionName}/players")
    public List<PlayerResponse> retrievePlayers(@PathVariable String auctionName,
                                                @RequestParam(required = false) String playerName,
                                                @RequestParam(required = false) String playerRole,
                                                @RequestParam(required = false) String playerStatus,
                                                @RequestParam(required = false) String teamName,
                                                @RequestParam int page,
                                                @RequestParam int size) {

        Pageable pageRequest = PageRequest.of(page, size);
        RetrievePlayersQuery request = RetrievePlayersQuery.builder()
                .auctionName(auctionName)
                .playerRole(playerRole)
                .playerName(playerName)
                .playerStatus(playerStatus)
                .teamName(teamName)
                .pageable(pageRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Update player by player id and auction id")
    @PutMapping("/{auctionName}/players")
    public PlayerResponse updatePlayer(@PathVariable String auctionName,
                                       @RequestParam String playerId,
                                       @RequestBody PlayerRequest playerRequest) {
        UpdatePlayerQuery request = UpdatePlayerQuery.builder()
                .auctionName(auctionName)
                .playerId(playerId)
                .playerRequest(playerRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Delete player by player id and auction id")
    @DeleteMapping("/{auctionName}/players")
    public String deletePlayer(@PathVariable String auctionName,
                               @RequestParam String playerId) {
        DeletePlayerQuery request = DeletePlayerQuery.builder()
                .auctionName(auctionName)
                .playerId(playerId)
                .build();
        return queryExecutor.execute(request);
    }
}
