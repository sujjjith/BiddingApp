package com.bid.service;

import com.bid.data.Item;
import com.bid.data.User;
import com.bid.handler.Bid;
import com.bid.utility.DatePattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by skupunarapu on 1/8/2016.
 */
public class BidService {

    private Long nextId;
    private Map<Long, Bid> bidMap;

    public BidService() {
        bidMap = new ConcurrentHashMap<Long, Bid>();
        nextId = 0L;
    }

    public long createAuction(Item item, String closingTimeString, User owner){
        long id;
        synchronized (nextId) {
            id = nextId;
            nextId++;
        }
        System.out.println("Creating auction no. " + id);

        Date closingTime;
        try {
            SimpleDateFormat sdf = DatePattern.getSdf();
            closingTime = sdf.parse(closingTimeString);
        } catch (ParseException e) {
            System.out.println("Closing time couldn't be parsed. Did you enter a correctly formatted date?");
            return -1;
        }

        if (closingTime == null)
            return -1;

        Bid bid = new Bid(item, closingTime, id, owner);
        System.out.println("Auction created: " + bid.getShortDescription());

        bidMap.put(id, bid);

        return id;
    }

    public boolean bid(User bidder, long auctionId, double bid){
        Bid a = bidMap.get(auctionId);
        if (a == null) {
            System.out.println("Wrong auction ID!");
            return false;
        }
        a.bid(bidder, bid);
        return true;
    }
}
