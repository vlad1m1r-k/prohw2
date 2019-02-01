package ua.kiev.prog.hw2.controller;

import java.util.Objects;

public class OnlineUser {
    private String login;
    private String room;

    public OnlineUser(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineUser that = (OnlineUser) o;
        return login.toLowerCase().equals(that.login.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(login.toLowerCase());
    }
}
