package ru.aso.filenet.utils.domain;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.04.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public abstract class P8AbstractModel implements Serializable {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
