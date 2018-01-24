package ee.testijad.mobilecpp.tests;

import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.XmlFileBuilder;

public class GenerateSuite {

    public static void main(String[] args) {
        String className = "ee.testijad.mobilecpp.tests.ValidationTests";
        XmlFileBuilder.generateTestSuite(Config.DATA_FILES_DIRECTORY, className);
    }
}
