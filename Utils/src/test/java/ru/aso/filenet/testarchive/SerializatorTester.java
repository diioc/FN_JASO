package ru.aso.filenet.testarchive;

import ru.aso.filenet.utils.domain.P8ClassModel;
import ru.aso.filenet.utils.implementation.UFactory;
import ru.aso.filenet.utils.render.SimpleTxtRenderer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: asolynin
 * Date: 15.10.13
 * Time: 15:21
 * To change this template use File | Settings | File Templates.
 */
public class SerializatorTester {
    private static final String FILE_INPUT_PATH = "D:\\fullSzmnDocModel.p8m";
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(FILE_INPUT_PATH);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List<P8ClassModel> modelList = (List<P8ClassModel>)ois.readObject();

        SimpleTxtRenderer renderer = new SimpleTxtRenderer();
        for (P8ClassModel model : modelList) {
            renderer.printModel(model);
        }
    }
}
