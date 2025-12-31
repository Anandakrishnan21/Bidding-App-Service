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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {
    private final QueryExecutor queryExecutor;

    @Autowired
    public PlayerController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @ApiOperation("Create new player by auction name")
    @PostMapping("/{auctionId}/players")
    public PlayerResponse createPlayer(@PathVariable String auctionId,
                                       @RequestBody PlayerRequest playerRequest) {
        CreatePlayerQuery request = CreatePlayerQuery.builder()
                .auctionId(auctionId)
                .playerRequest(playerRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Retrieve players based on the query")
    @GetMapping("/{auctionId}/players")
    public List<PlayerResponse> retrievePlayers(@PathVariable String auctionId,
                                                @RequestParam(required = false) String playerName,
                                                @RequestParam(required = false) String playerRole,
                                                @RequestParam(required = false) String playerStatus,
                                                @RequestParam(required = false) String teamName) {
        RetrievePlayersQuery request = RetrievePlayersQuery.builder()
                .auctionId(auctionId)
                .playerRole(playerRole)
                .playerName(playerName)
                .playerStatus(playerStatus)
                .teamName(teamName)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Update player by player id and auction name")
    @PutMapping("/{auctionId}/players")
    public PlayerResponse updatePlayer(@PathVariable String auctionId,
                                       @RequestParam String playerId,
                                       @RequestBody PlayerRequest playerRequest) {
        UpdatePlayerQuery request = UpdatePlayerQuery.builder()
                .auctionId(auctionId)
                .playerId(playerId)
                .playerRequest(playerRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation("Delete player by player id and auction name")
    @DeleteMapping("/{auctionId}/players")
    public String deletePlayer(@PathVariable String auctionId,
                               @RequestParam String playerId) {
        DeletePlayerQuery request = DeletePlayerQuery.builder()
                .auctionId(auctionId)
                .playerId(playerId)
                .build();
        return queryExecutor.execute(request);
    }
}
