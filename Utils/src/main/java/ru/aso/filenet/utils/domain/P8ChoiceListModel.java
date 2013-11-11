package ru.aso.filenet.utils.domain;

import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.04.13
 * Time: 11:14
 * To change this template use File | Settings | File Templates.
 */
public class P8ChoiceListModel extends P8AbstractModel {
    private String displayName;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int typeAsInt;
    private Properties choiceItems;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTypeAsInt() {
        return typeAsInt;
    }

    public void setTypeAsInt(int typeAsInt) {
        this.typeAsInt = typeAsInt;
    }

    public Properties getChoiceItems() {
        return choiceItems;
    }

    public void setChoiceItems(Properties choiceItems) {
        this.choiceItems = choiceItems;
    }
}
