package ru.aso.filenet.utils.domain;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: asolynin
 * Date: 17.10.13
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class DeletedEntity {

    public DeletedEntity(){}

    public DeletedEntity(String unqName, int entityType, Set<String> linkedObjectsIds) {
        this.unqName = unqName;
        this.entityType = entityType;
        this.linkedObjectsIds = linkedObjectsIds;
    }

    public String getUnqName() {
        return unqName;
    }

    public void setUnqName(String unqName) {
        this.unqName = unqName;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Set<String> getLinkedObjectsIds() {
        return linkedObjectsIds;
    }

    public void setLinkedObjectsIds(Set<String> linkedObjectsIds) {
        this.linkedObjectsIds = linkedObjectsIds;
    }

    private String unqName;
    private int entityType;
    private Set<String> linkedObjectsIds;

    public static final int CHOICELIST = 1;
    public static final int PROPERTYTEMPLATE = 2;
}
