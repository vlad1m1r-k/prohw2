package ua.kiev.prog.hw2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class MessageList {
    private static final MessageList msgList = new MessageList();

    private final Gson gson;
    private final List<Message> list = new LinkedList<>();

    public static MessageList getInstance() {
        return msgList;
    }

    private MessageList() {
        gson = new GsonBuilder().create();
    }

    public synchronized void add(Message m) {
        list.add(m);
    }

    public synchronized String toJSON(int n, String to, String room) {
        if (n >= list.size()) return null;
        return gson.toJson(new JsonMessages(list, n, to, room));
    }
}