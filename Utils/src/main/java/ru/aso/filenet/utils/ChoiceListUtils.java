package ru.aso.filenet.utils;

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.core.ObjectStore;
import ru.aso.filenet.utils.domain.P8ChoiceListModel;

/**
 * Created with IntelliJ IDEA.
 * User: ASolynin
 * Date: 25.04.13
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public interface ChoiceListUtils {
    public P8ChoiceListModel getModel(ChoiceList choiceList);
    public void createChoiceList(ObjectStore os, P8ChoiceListModel choiceListModel, boolean overwriteIfExist);
    public void deleteChoiceList(ObjectStore os, P8ChoiceListModel choiceListModel);
}
