package ee.testijad.mobilecpp.util;

import ee.testijad.mobilecpp.validation.IgnoredFiles;
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

public class TestSuiteBuilder {

    private static final String[] SIGNATURE_FILE_EXTENSIONS = new String[]{"ddoc", "bdoc", "adoc", "edoc", "asice", "asics", "sce", "scs"};

    public static void generateTestSuite(List<String> fileNames, String className) {
        XmlSuite suite = new XmlSuite();
        suite.setName("Data files validation suite");
        suite.setVerbose(2);
        for (String fileName : fileNames) {
            XmlTest test = new XmlTest(suite);
            test.setName(fileName);
            test.addParameter("fileName", getFileNameParameter(fileName));
            List<XmlClass> xmlClasses = new ArrayList<>();
            XmlClass xmlClass = new XmlClass(className, false);
            List<XmlInclude> xmlInclude = new ArrayList<>();
            xmlInclude.add(0, new XmlInclude("validateTestFile"));
            xmlClass.setIncludedMethods(xmlInclude);
            xmlClasses.add(xmlClass);
            test.setXmlClasses(xmlClasses);
        }
        saveSuiteFile(suite, Config.TEST_SUITE_FILE_DIRECTORY);
    }

    public static void generateTestSuite(String directory, String className) {
        generateTestSuite(getFileNamesListFromDirectory(directory), className);
    }

    private static List<String> getFileNamesListFromDirectory(String directory) {
        File folder = new File(directory);
        try {
            Log.info("Getting all files in " + folder.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOFileFilter filter = new SuffixFileFilter(SIGNATURE_FILE_EXTENSIONS, IOCase.INSENSITIVE);
        Iterator<File> iterator = FileUtils.iterateFiles(folder, filter, DirectoryFileFilter.DIRECTORY);
        List<String> fileNames = new ArrayList<>();
        while (iterator.hasNext()) {
            fileNames.add(iterator.next().getName());
        }
        return fileNames;
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

    private static String getFileNameParameter(String fileName) {
        String parameterName = fileName;
        if (IgnoredFiles.isIgnored(parameterName)) {
            parameterName += IgnoredFiles.getIgnoreMessage(parameterName);
        }
        return parameterName;
    }

}
