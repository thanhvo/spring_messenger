package com.gfgtech.sc.spring_workers.service;

import com.gfgtech.sc.spring_workers.listener.ReactiveSqsListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessengerService {
    @Autowired
    private List<ReactiveSqsListener> listeners;

    public MessengerService(List<ReactiveSqsListener> listeners) {
        this.listeners = listeners;
    }
}
