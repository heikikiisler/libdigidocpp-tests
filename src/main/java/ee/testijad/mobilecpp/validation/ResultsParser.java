package ee.testijad.mobilecpp.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.testijad.mobilecpp.util.Config;
import ee.testijad.mobilecpp.util.Log;
import ee.testijad.mobilecpp.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ResultsParser {

    private static final String WARNINGS_SEPARATOR = "\\n";
    private static final String JSON_FILENAME_KEY = "f";
    private static final String JSON_RESULT_KEY = "s";
    private static final String JSON_WARNINGS_KEY = "d";
    private List<Map<String, String>> results;
    private String versionInfo;
    private String timestamp;

    private ResultsParser(String resultsFilePath) {
        String contents = Utils.readFileIntoString(resultsFilePath);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readValue(contents, JsonNode.class);
            versionInfo = node.get("version").asText();
            timestamp = node.get("start").asText();
            JsonNode resultNode = node.get("result");
            results = mapper.readValue(resultNode.toString(), new TypeReference<List<Map<String, String>>>() {});
            Log.info(String.format("[Lib version] libdigidocpp version: %s from file: %s", versionInfo, resultsFilePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ResultsParser getDefault() {
        String resultsFileName = System.getProperty("mobilecpp.results-file");
        if (resultsFileName == null) {
            resultsFileName = getLatestResultsFileName();
        }
        return new ResultsParser(String.format("%s/%s", Config.RESULT_FILES_DIRECTORY, resultsFileName));
    }

    private static String getLatestResultsFileName() {
        File folder = new File(Config.RESULT_FILES_DIRECTORY);
        String extension = "json";
        IOFileFilter filter = new SuffixFileFilter(extension, IOCase.INSENSITIVE);
        Iterator<File> files = FileUtils.iterateFiles(folder, filter, DirectoryFileFilter.DIRECTORY);
        List<String> fileNames = new ArrayList<>();
        files.forEachRemaining(file -> fileNames.add(file.getName()));
        Collections.sort(fileNames);
        String latestResultsFileName = fileNames.get(fileNames.size() - 1);
        Log.info("Using results from " + latestResultsFileName);
        return latestResultsFileName;
    }

    public FileResult getTestFileResult(TestFile testFile) {
        for (Map<String, String> result : results) {
            if (result.get(JSON_FILENAME_KEY).equals(testFile.getFileName())) {
                String resultString = result.get(JSON_RESULT_KEY);
                String warningString = result.get(JSON_WARNINGS_KEY);
                Set<String> warnings = Utils.getWarningSetFromString(warningString, WARNINGS_SEPARATOR);
                return new FileResult(ResultType.get(resultString), warnings);
            }
        }
        throw new RuntimeException(String.format("Could not find test file \"%s\" from results", testFile.getFileName()));
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
