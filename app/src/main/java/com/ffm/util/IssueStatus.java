package com.ffm.util;

public enum IssueStatus {
    NEW("NEW"), ACCEPTED("ACCEPTED"), ASSIGNED("ASSIGNED"), NOT_ACCEPTED("NOT ACCEPTED"),IN_PROGRESS("IN PROGRESS"),
    STARTED("STARTED"), NOT_STARTED("NOT STARTED"), PAUSED("PAUSED"), COMPLETED("COMPLETED"), RE_OPENED("RE-OPENED");

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    IssueStatus(String value) {
        this.value = value;
    }

    public static IssueStatus getIssueStatus(String status) {
        for (int i = 0; i < values().length; ++i) {
            if (values()[i].getValue().equals(status)) {
                return values()[i];
            }
        }
        return values()[0];
    }
}
