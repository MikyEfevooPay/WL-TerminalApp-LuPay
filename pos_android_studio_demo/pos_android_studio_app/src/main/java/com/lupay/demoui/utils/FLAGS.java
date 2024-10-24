package com.lupay.demoui.utils;

public enum FLAGS {
    CHECK_NETWORK(0);

    private int value;
    FLAGS(int value) {
        this.value = value;
    }

    public int getValue() {return value;}
}
