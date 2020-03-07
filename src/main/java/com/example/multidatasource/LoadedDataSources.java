package com.example.multidatasource;

public enum LoadedDataSources {
    PRIMARY("primary"),

    FIRST("first");

    private String name;

    LoadedDataSources(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "LoadedDataSources{" +
                "name='" + name + '\'' +
                '}';
    }
}
