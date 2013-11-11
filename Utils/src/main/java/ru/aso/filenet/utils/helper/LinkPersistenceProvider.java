package ru.aso.filenet.utils.helper;

import java.util.Set;

/**
 * User: asolynin
 * Date: 17.10.13
 * Time: 14:31
 */
public interface LinkPersistenceProvider {
    public void pushDeletedEntity(String unqName, int entityType, String id);
    public boolean hasEntity(String unqName, int entityType);
    public Set<String> pullLinkedObjects(String unqName, int entityType);
}
