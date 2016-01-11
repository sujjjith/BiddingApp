package com.bid.timer;

import com.bid.handler.Bid;

import java.util.TimerTask;

/**
 * Created by skupunarapu on 1/8/2016.
 */
public class EndTimer extends TimerTask{

    private Bid bid;

    public EndTimer(Bid bid) {
        this.bid = bid;
    }

    @Override
    public void run() {
        System.out.println("Timer started!");
        bid.close();
    }
}
