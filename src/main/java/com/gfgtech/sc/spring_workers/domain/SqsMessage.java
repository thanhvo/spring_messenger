package com.gfgtech.sc.spring_workers.domain;

public class SqsMessage {
    private String id;
    private String message;

    public SqsMessage()
    {
        this.id = null;
        this.message = null;
    }

    public SqsMessage(String id, String message)
    {
        this.id = id;
        this.message = message;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
