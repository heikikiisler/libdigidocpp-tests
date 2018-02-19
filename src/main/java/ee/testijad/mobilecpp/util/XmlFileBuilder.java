package ee.testijad.mobilecpp.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlFileBuilder {

    public static void generateTestSuite(String directory, String className) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Data files validation suite");
        suite.setVerbose(2);
        Iterator<File> myFiles = fileListBuilder(directory);
        String fileName;
        while (myFiles.hasNext()) {
            fileName = myFiles.next().getName();
            // Log.info(String.format("File name: %s", fileName));
            XmlTest myTest = new XmlTest(suite);
            myTest.setName(fileName);
            myTest.addParameter("fileName", fileName);
            List<XmlClass> xmlClasses = new ArrayList<>();
            XmlClass clazz = new XmlClass(className, false);
            List<XmlInclude> xmlInclude = new ArrayList<>();
            xmlInclude.add(0, new XmlInclude("validateTestFile"));
            clazz.setIncludedMethods(xmlInclude);
            xmlClasses.add(clazz);
            myTest.setXmlClasses(xmlClasses);
        }
        saveSuiteFile(suite, Config.TEST_SUITE_FILE_DIRECTORY);
    }

    private static Iterator<File> fileListBuilder(String directory) {
        File folder = new File(directory);
        String[] extensions = new String[]{"ddoc", "bdoc", "adoc", "edoc", "asice", "asics", "sce", "scs"};
        try {
            Log.info("Getting all files in " + folder.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOFileFilter filter = new SuffixFileFilter(extensions, IOCase.INSENSITIVE);
        return FileUtils.iterateFiles(folder, filter, DirectoryFileFilter.DIRECTORY);
    }

    private static void saveSuiteFile(XmlSuite suite, String filePath) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try {
            FileWriter fileWriter = new FileWriter(file, false); // false to overwrite
            fileWriter.write(suite.toXml());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
