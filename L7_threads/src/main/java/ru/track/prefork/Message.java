package ru.track.prefork;

import java.io.Serializable;

public class Message implements Serializable {
    public String username;
    public String data;

    public Message(String username, String data) {
        this.username = username;
        this.data = data;
    }

    public Message(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return username + '>' + data;
    }
}
