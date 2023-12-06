package com.esprow.interview.sklroman.stockmarket.error;

import java.util.Date;

public class ErrorInfo {
    private Date timestamp;
    private String errorMessage;

    public ErrorInfo(String message) {
        this.timestamp = new Date();
        this.errorMessage = message.split(";")[0];
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
