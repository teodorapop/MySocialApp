package com.example.mysocialapp.utils.events;

import com.example.mysocialapp.domain.Request;


public class RequestEvent implements Event {
    private final ChangeEventType type;
    private final Request data;
    private Request oldData;

    public RequestEvent(ChangeEventType type, Request data) {
        this.type = type;
        this.data = data;
    }
    public RequestEvent(ChangeEventType type, Request data, Request oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Request getData() {
        return data;
    }

    public Request getOldData() {
        return oldData;
    }
}

