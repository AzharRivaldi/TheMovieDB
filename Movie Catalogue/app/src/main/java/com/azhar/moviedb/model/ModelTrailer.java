package com.azhar.moviedb.model;

import java.io.Serializable;

/**
 * Created by Azhar Rivaldi on 22-12-2019.
 */

public class ModelTrailer implements Serializable {

    private String key;
    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
