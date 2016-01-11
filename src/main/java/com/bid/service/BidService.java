package com.bid.service;

import com.bid.data.Item;
import com.bid.data.User;
import com.bid.handler.Bid;
import com.bid.utility.DatePattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by skupunarapu on 1/8/2016.
 */
public class BidService {

    private static final int STORING_TIME_AMOUNT = 2;
    private static final int STORING_TIME_UNIT= GregorianCalendar.MINUTE;


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

        Date closingTime = null;
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

    public String getSdfString(){
        return DatePattern.SDF_PATTERN;
    }

    public String getAuctionListString(){
        String s = String.format("%-15s%-20s%-20s%-20s%-20s\n", "ID", "Title",
                "Price (in GDB)", "Closing Time", "Open?");
        s += "-------------------------------------------------------------------------------\n";

        Calendar cal = new GregorianCalendar();
        for (Map.Entry<Long, Bid> e: bidMap.entrySet()) {
            Long key = e.getKey();
            Bid a = e.getValue();
            cal.setTime(a.getClosingTime());
            cal.add(STORING_TIME_UNIT, STORING_TIME_AMOUNT);
            if ((new GregorianCalendar().after(cal))) {
                System.out.println("Removing auction " + key);
                bidMap.remove(key);
                continue;
            }
            s += a.getShortDescription() + "\n";
        }
        return s;
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

//    public String getAuctionDetails(long auctionId){
//        Bid a = bidMap.get(auctionId);
//        if (a == null)
//            return null;
//        return a.getFullDescription();
//    }
}
