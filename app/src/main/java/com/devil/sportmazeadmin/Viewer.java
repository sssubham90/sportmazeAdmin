package com.devil.sportmazeadmin;

public class Viewer {
    private String name;
    private String key;

    public Viewer(String name, String value) {
        this.name = name;
        this.key = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
