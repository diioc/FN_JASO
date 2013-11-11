package ru.aso.filenet.utils.implementation;

import ru.aso.filenet.utils.ChoiceListUtils;
import ru.aso.filenet.utils.ClassDefinitionUtils;
import ru.aso.filenet.utils.PropertyUtils;
import ru.aso.filenet.utils.helper.ConnectionProvider;
import ru.aso.filenet.utils.helper.ConnectionProviderImpl;
import ru.aso.filenet.utils.helper.LinkPersistenceProvider;

/**
 * User: ASO
 * Date: 22.05.13
 * Time: 0:07
 */
public class UFactory {
    public static final ChoiceListUtils choiceList = new ChoiceListUtilsImpl();
    public static final ClassDefinitionUtils classDefinition = new ClassDefinitionUtilsImpl();
    public static final PropertyUtils property = new PropertyUtilsImpl();
    public static final ConnectionProvider connection = new ConnectionProviderImpl();
    protected static final LinkPersistenceProvider deletedEntity = new LinkPersistenceProviderImpl();
}
