package com.gfgtech.sc.spring_workers.service;

import com.gfgtech.sc.spring_workers.listener.ReactiveSqsListener;
import com.gfgtech.sc.spring_workers.publisher.SqsPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublishingService {
    @Autowired
    private List<SqsPublisher> publishers;

    public PublishingService(List<SqsPublisher> publishers) {
        this.publishers = publishers;
    }
}