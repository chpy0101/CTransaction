package com.ctransaction.contract.entity;

/**
 * Created by chpy on 19/5/13.
 */
public enum NodeStatus {
    HEALTH(1),
    FREEZE(0),
    DEAD(-1)
    ;

    NodeStatus(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
