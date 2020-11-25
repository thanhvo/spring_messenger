package com.gfgtech.sc.spring_workers.domain;

public class SqsMessage {
    private String id;
    private String message;

    public SqsMessage(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
