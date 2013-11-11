package ru.aso.filenet.utils.domain;


/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 24.04.13
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class P8PropertyModel extends P8AbstractModel {
    private String displayName;
    private String symbolicName;
    private String description;
    private String typeAsString;
    private String defaultValue;
    private String cardinalityAsString;
    private int typeAsInt;
    private int maxSize;
    private int cardinalityAsInt;
    private boolean required;
    private boolean hidden;
    private boolean choiceList;
    private P8ChoiceListModel choiceListModel;

    public P8ChoiceListModel getChoiceListModel() {
        return choiceListModel;
    }

    public void setChoiceListModel(P8ChoiceListModel choiceListModel) {
        this.choiceListModel = choiceListModel;
    }

    public String getCardinalityAsString() {
        return cardinalityAsString;
    }

    public void setCardinalityAsString(String cardinalityAsString) {
        this.cardinalityAsString = cardinalityAsString;
    }

    public int getCardinalityAsInt() {
        return cardinalityAsInt;
    }

    public void setCardinalityAsInt(int cardinalityAsInt) {
        this.cardinalityAsInt = cardinalityAsInt;
    }

    public boolean hasChoiceList() {
        return choiceList;
    }

    public void setChoiceList(boolean choiceList) {
        this.choiceList = choiceList;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
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

    public String getTypeAsString() {
        return typeAsString;
    }

    public void setTypeAsString(String typeAsString) {
        this.typeAsString = typeAsString;
    }

    public int getTypeAsInt() {
        return typeAsInt;
    }

    public void setTypeAsInt(int typeAsInt) {
        this.typeAsInt = typeAsInt;
    }
}
