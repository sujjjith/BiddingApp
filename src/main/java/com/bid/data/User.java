package com.bid.data;

import java.util.UUID;

/**
 * Created by skupunarapu on 1/6/2016.
 */
public class User {
    private String name;
    private UUID id;
    private boolean isAdmin;

    public User(String name, boolean isAdmin) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.isAdmin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id.toString();
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
