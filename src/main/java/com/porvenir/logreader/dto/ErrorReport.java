package com.porvenir.logreader.dto;

public class ErrorReport {
    private String errorType;
    private long count;

    public ErrorReport(String errorType, long count) {
        this.errorType = errorType;
        this.count = count;
    }

    public String getErrorType() { return errorType; }
    public long getCount() { return count; }
}
