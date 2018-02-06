package ee.testijad.mobilecpp.tests;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * Test listener to remove error stack traces from failed tests.
 */
public class TestListener extends TestListenerAdapter {

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable throwable = result.getThrowable();
        StackTraceElement[] stackTrace = new StackTraceElement[0];
        throwable.setStackTrace(stackTrace);
    }

}
