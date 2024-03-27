package com.example.mysocialapp.utils.observer;

import com.example.mysocialapp.utils.events.Event;


public interface Observer<E extends Event> {
    void update(E e);
}