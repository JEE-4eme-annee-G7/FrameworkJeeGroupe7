package com.esgi.framework_JEE.slot.web.response;

import com.esgi.framework_JEE.user.Domain.entities.User;

public class SlotResponse {
    public int id;
    public String start;
    public String end;
    public User user;

    public SlotResponse() {
    }

    public int getId() {
        return id;
    }

    public SlotResponse setId(int id) {
        this.id = id;
        return this;
    }

    public String getStart() {
        return start;
    }

    public SlotResponse setStart(String start) {
        this.start = start;
        return this;
    }

    public String getEnd() {
        return end;
    }

    public SlotResponse setEnd(String end) {
        this.end = end;
        return this;
    }
    public User getUser() {
        return user;
    }
    public SlotResponse setUser(User user) {
        this.user = user;
        return this;
    }
}
