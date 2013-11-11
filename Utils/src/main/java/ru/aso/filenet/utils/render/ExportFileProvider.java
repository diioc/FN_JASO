package ru.aso.filenet.utils.render;

import ru.aso.filenet.utils.domain.P8ClassModel;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asolynin
 * Date: 15.10.13
 * Time: 13:02
 * To change this template use File | Settings | File Templates.
 */
public interface ExportFileProvider {
    public void exportToFile(List<P8ClassModel> classDefinitionsList, String fileName);
}
