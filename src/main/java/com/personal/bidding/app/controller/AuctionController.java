package com.personal.bidding.app.controller;

import com.personal.bidding.app.model.request.AuctionRequest;
import com.personal.bidding.app.model.response.AuctionResponse;
import com.personal.bidding.app.service.AuctionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuctionController {
    @Autowired
    private AuctionService auctionService;

    @ApiOperation(value = "Create a new auction")
    @PostMapping("/auction")
    public AuctionResponse createAuction(@RequestBody AuctionRequest auctionRequest) {
        return auctionService.createAuction(auctionRequest);
    }
}
