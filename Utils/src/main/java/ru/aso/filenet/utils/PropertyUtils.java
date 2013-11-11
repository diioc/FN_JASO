package ru.aso.filenet.utils;

import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.admin.PropertyTemplate;
import ru.aso.filenet.utils.domain.P8PropertyModel;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.04.13
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public interface PropertyUtils {
    public P8PropertyModel getModel(PropertyTemplate propertyTemplate);
    public P8PropertyModel getModel(PropertyDefinition propertyDefinition);
    public String createPropertyTemplate(P8PropertyModel propertyModel, boolean overwriteIfExist);
    public boolean isExist(P8PropertyModel propertyModel);
}
