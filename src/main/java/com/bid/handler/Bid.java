package com.bid.handler;

import com.bid.data.Item;
import com.bid.data.User;
import com.bid.timer.EndTimer;
import com.bid.utility.DatePattern;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by skupunarapu on 1/6/2016.
 */
public class Bid {
    private Item item;
    private Date closingTime;

    private long id;
    private boolean isOpen;

    private User owner;
    private User highestBidder;
    private String highestBidderName;
    private Map<String, User> biddersMap;
    private EndTimer task;
    private Timer timer;

    public Bid(Item item, Date closingTime, long id, User owner) {
        this.item = item;
        this.closingTime = closingTime;
        this.id = id;
        this.owner = owner;
        timer = new Timer(true);
        task = new EndTimer(this);
        this.biddersMap = new HashMap<String, User>();
        closeBidding();
        if(!isOpen){
            System.out.println("Entered wrong date");
        }
    }

    public synchronized void bid(User bidder, double bid) {
            if (!isOpen) {
                System.out.println("Auction is no longer open! Sorry...");
                return;
            }
            double itemValue = item.getItemValue();
            String bidderId = bidder.getId();
            biddersMap.put(bidderId, bidder);
            if (bid <= itemValue) {
                System.out.println("Bid not high enough");
                return;
            }
            highestBidder = bidder;
            highestBidderName = bidder.getName();
            item.setItemValue(bid);
            System.out.println("Bid accepted");
    }

    public synchronized String getShortDescription() {
        String closingTimeString = DatePattern.getSdf().format(closingTime);
        return String.format("%-15d%-20s%-20.2f%-20s%-10s", id, item.getItemName(),
                item.getItemValue(), closingTimeString, Boolean.toString(isOpen));
    }

    private String getResult() {
        if (highestBidder == null)
            return "Price not met.";

        return String.format("Item purchased for %.2fGBP by %s.", item.getItemValue(), highestBidderName);
    }

    private synchronized void closeBidding() {
        if (closingTime.before(new Date())) {
            this.isOpen = false;
        } else {
            this.isOpen = true;
            timer.schedule(task, closingTime);
            System.out.println("Timer set!");
        }
    }

    public synchronized Date getClosingTime() {
        return closingTime;
    }

    public void close() {
        System.out.println("Closing Auction " + id);

        isOpen = false;
        if (highestBidder == null) {
            System.out.println("No bidders for closed auction " + id);
            System.out.println(String.format("Unfortunately the value of the auction item \"%s\" was not met",
                    item.getItemName()));
            for (User bidder: biddersMap.values()) {
                System.out.println(String.format("%s auction closed and nobody met the starting value %.2fGBP!",
                        item.getItemName(), item.getItemValue()));
            }

        } else {
            System.out.println(String.format("Auction %d is won with value %.2fGBP by %s", id, item.getItemValue(), highestBidderName));
            System.out.println(String.format("Your item %s has been sold for %.2fGBP to %s",
                    item.getItemName(), item.getItemValue(), highestBidderName));
            for (Map.Entry<String, User> e: biddersMap.entrySet()) {
                String id = e.getKey();
                User bidder = e.getValue();
                if (bidder == highestBidder)
                    System.out.println(String.format("Congratulations! You purchased the %s auction item for %.2fGBP!",
                            item.getItemName(), item.getItemValue()));
                else
                    System.out.println(String.format("\"%s\" auction item was purchased by %s for %.2fGBP",
                            item.getItemName(), highestBidderName, item.getItemValue()));
            }
        }
    }
}
