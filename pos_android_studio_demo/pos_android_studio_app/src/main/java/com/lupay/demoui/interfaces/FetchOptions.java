package com.lupay.demoui.interfaces;

public class FetchOptions {
    public int method;
    public String URL;

    public FetchOptions(String _URL, int _method) {
        this.method = _method;
        this.URL = _URL;
    }
}
