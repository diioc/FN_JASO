package ru.aso.filenet.utils.implementation;

import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.DeletedEntity;
import ru.aso.filenet.utils.helper.LinkPersistenceProvider;

import java.util.*;

/**
 * User: asolynin
 * Date: 17.10.13
 * Time: 14:55
 */
class LinkPersistenceProviderImpl implements LinkPersistenceProvider {
    private static final Logger logger = Logger.getLogger(LinkPersistenceProviderImpl.class);
    private List<DeletedEntity> entityList;

    public LinkPersistenceProviderImpl() {
        entityList = new LinkedList<DeletedEntity>();
        logger.trace("Created link persistence provider with list of deleted entities.");
    }

    @Override
    public void pushDeletedEntity(String unqName, int entityType, String id) {
        DeletedEntity entity = getEntity(unqName, entityType);
        if (entity == null) {
            entity = new DeletedEntity(unqName, entityType, new HashSet<String>());
            entityList.add(entity);
            logger.trace("Create new deleted entity [" + unqName + "]. Currently list have " + entityList.size() + " entities.");
        }
        entity.getLinkedObjectsIds().add(id);
        logger.trace("Added in deleted entity [" + unqName + "] new ID = " + id);

    }

    @Override
    public boolean hasEntity(String unqName, int entityType) {
        for (DeletedEntity entity : entityList) {
            int currentType = entity.getEntityType();
            String currentName = entity.getUnqName();
            if (currentName.equals(unqName) && currentType == entityType)
                return true;
        }
        return false;
    }

    @Override
    public Set<String> pullLinkedObjects(String unqName, int entityType) {
        logger.trace("Getting deletedEntity set [" + unqName + "]");
        Iterator<DeletedEntity> entityIterator = entityList.iterator();
        while (entityIterator.hasNext()) {
            DeletedEntity entity = entityIterator.next();
            if (entity.getUnqName().equals(unqName) && entity.getEntityType() == entityType) {
                entityIterator.remove();
                logger.trace("Returning set. Currently list have " + entityList.size() + " entities.");
                return entity.getLinkedObjectsIds();
            }
        }
        logger.warn("Returning null.");
        return null;
    }

    private DeletedEntity getEntity(String unqName, int entityType) {
        for (DeletedEntity entity : entityList) {
            if (entity.getUnqName().equals(unqName) && entity.getEntityType() == entityType)
                return entity;
        }
        return null;
    }

}
