package ru.aso.filenet.utils;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.core.ObjectStore;
import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.domain.P8PropertyModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 21.03.13
 * Time: 18:06
 * To change this template use File | Settings | File Templates.
 */
public interface ClassDefinitionUtils {
    public List<ClassDefinition> getAllChildren(ClassDefinition classDefinition);
    public P8ClassModel getModel(ClassDefinition classDefinition, boolean includeInheritedProps);
    public String createClassDefinition(ObjectStore os, P8ClassModel classModel, boolean overwriteIfExist);
    public void addProperty(ClassDefinition classDefinition, P8PropertyModel propertyModel);
    public void removeProperty(ClassDefinition classDefinition, P8PropertyModel propertyModel);
    public boolean isExist(ClassDefinition classDefinition);
}
