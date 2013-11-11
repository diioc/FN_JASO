package ru.aso.filenet.utils.domain;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 24.04.13
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public class P8ClassModel extends P8AbstractModel {
    private String parentClassSymbolicName;
    private String displayName;
    private String symbolicName;
    private String description;
    private List<P8PropertyModel> properties;

    public List<P8PropertyModel> getProperties() {
        return properties;
    }

    public void setProperties(List<P8PropertyModel> properties) {
        this.properties = properties;
    }

    public String getParentClassSymbolicName() {
        return parentClassSymbolicName;
    }

    public void setParentClassSymbolicName(String parentClassSymbolicName) {
        this.parentClassSymbolicName = parentClassSymbolicName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
