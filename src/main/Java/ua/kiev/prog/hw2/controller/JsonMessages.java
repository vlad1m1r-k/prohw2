package ua.kiev.prog.hw2.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonMessages {
    private final List<Message> list;
    private int last;

    public JsonMessages(List<Message> sourceList, int fromIndex, String to, String room) {
        this.list = new ArrayList<>();
        last = sourceList.size();
        for (int i = fromIndex; i < sourceList.size(); i++) {
            Message msg = sourceList.get(i);
            if (msg.getTo().equals("All") || msg.getTo().toLowerCase().equals(to.toLowerCase()) || (room != null && room.equals(msg.getTo()))) {
                msg.clearToken();
                list.add(msg);
            }
        }
    }

    public List<Message> getList() {
        return Collections.unmodifiableList(list);
    }

    public int getLast() {
        return last;
    }
}