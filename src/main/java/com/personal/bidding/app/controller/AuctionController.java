package com.personal.bidding.app.controller;

import com.personal.bidding.app.model.request.AuctionRequest;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.service.AuctionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @ApiOperation(value = "Retrieve all auctions details")
    @GetMapping("/auctions")
    public List<AuctionResponse> retrieveAllAuction() {
        return auctionService.retrieveAllAuction();
    }

    @ApiOperation(value = "Create a new auction")
    @PostMapping("/auction")
    public AuctionResponse createAuction(@RequestBody AuctionRequest auctionRequest) {
        return auctionService.createAuction(auctionRequest);
    }

    @ApiOperation(value = "Retrieve auction by auction name")
    @GetMapping("/auction/{auctionId}")
    public AuctionResponse retrieveAuctionByName(@RequestParam String auctionId) {
        return auctionService.retrieveAuction(auctionId);
    }

    @ApiOperation(value = "Update auction by auction name")
    @PutMapping("/auction/{auctionId}")
    public AuctionResponse updateAuctionByName(@RequestParam String auctionId, @RequestBody AuctionRequest auctionRequest) {
        return auctionService.updateAuction(auctionId, auctionRequest);
    }

    @ApiOperation(value = "Delete auction by auction name")
    @DeleteMapping("/auction/{auctionId}")
    public String updateAuctionByName(@RequestParam String auctionId) {
        return auctionService.deleteAuction(auctionId);
    }
}
