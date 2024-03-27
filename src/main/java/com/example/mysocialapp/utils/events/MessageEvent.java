package com.example.mysocialapp.utils.events;

import com.example.mysocialapp.domain.Message;


public class MessageEvent implements Event {
    private final ChangeEventType type;
    private final Message data;
    private Message oldData;

    public MessageEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }
    public MessageEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}
