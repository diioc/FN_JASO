package ru.aso.filenet.utils.render;

import org.apache.log4j.Logger;
import ru.aso.filenet.utils.domain.P8ClassModel;

import java.io.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asolynin
 * Date: 15.10.13
 * Time: 13:13
 */
public class ModelSerializer implements ExportFileProvider {
    private static Logger logger = Logger.getLogger(ModelSerializer.class);

    @Override
    public void exportToFile(List<P8ClassModel> classDefinitionsList, String fileName) {
        if (!(classDefinitionsList instanceof Serializable))
            throw new IllegalArgumentException("Error, list implementation object must implement 'Serializable'");
        logger.trace("Start object serialization in file " + fileName);

        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(classDefinitionsList);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            logger.error("File not found.", e);
        } catch (IOException e) {
            logger.error("IO Exception.", e);
        }
    }
}
