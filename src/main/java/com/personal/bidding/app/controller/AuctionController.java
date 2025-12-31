package com.personal.bidding.app.controller;

import com.personal.bidding.app.model.query.*;
import com.personal.bidding.app.model.request.AuctionRequest;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.model.response.BiddingResponse;
import com.personal.bidding.app.service.utils.QueryExecutor;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuctionController {
    private final QueryExecutor queryExecutor;

    @Autowired
    public AuctionController(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    @ApiOperation(value = "Create a new auction")
    @PostMapping("/auction")
    public AuctionResponse createAuction(@RequestBody AuctionRequest auctionRequest) {
        CreateAuctionQuery request = CreateAuctionQuery.builder()
                .auctionName(auctionRequest.getAuctionName())
                .maxPlayers(auctionRequest.getMaxPlayers())
                .minimumBid(auctionRequest.getMinimumBid())
                .sportsType(auctionRequest.getSportsType())
                .teamPoints(auctionRequest.getTeamPoints())
                .increaseRate(auctionRequest.getIncreaseRate())
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation(value = "Retrieve auction by auction name")
    @GetMapping("/auction/{auctionId}")
    public AuctionResponse retrieveAuctionByName(@PathVariable String auctionId) {
        return queryExecutor.execute(RetrieveAuctionQuery.builder()
                .auctionId(auctionId)
                .build());
    }

    @ApiOperation(value = "Retrieve all auction details")
    @GetMapping("/auctions")
    public List<AuctionResponse> retrieveAllAuction() {
        return queryExecutor.execute(RetrieveAllAuctionQuery.builder().build());
    }

    @ApiOperation(value = "Update auction by auction name")
    @PutMapping("/auction/{auctionId}")
    public AuctionResponse updateAuctionByName(@PathVariable String auctionId,
                                               @RequestBody AuctionRequest auctionRequest) {
        UpdateAuctionQuery request = UpdateAuctionQuery.builder()
                .auctionId(auctionId)
                .auctionRequest(auctionRequest)
                .build();
        return queryExecutor.execute(request);
    }

    @ApiOperation(value = "Delete auction by auction name")
    @DeleteMapping("/auction/{auctionId}")
    public String updateAuctionByName(@PathVariable String auctionId) {
        return queryExecutor.execute(DeleteAuctionQuery.builder()
                .auctionId(auctionId)
                .build());
    }

    @PutMapping("/auction/{auctionId}/bidPlayer")
    public BiddingResponse bidPlayer(@PathVariable String auctionId,
                                     @RequestParam String auctionName,
                                     @RequestParam String teamName,
                                     @RequestParam String playerId,
                                     @RequestParam double soldPrice) {
        PlayerBiddingQuery request = PlayerBiddingQuery.builder()
                .auctionId(auctionId)
                .auctionName(auctionName)
                .teamName(teamName)
                .playerId(playerId)
                .soldPrice(soldPrice)
                .build();
        return queryExecutor.execute(request);
    }
}
