package configuration;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class CustomTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // Code to execute when a test starts
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        // Code to execute when a test succeeds
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Code to execute when a test fails
        System.out.println("Test failed: " + result.getName());

        // Example: Capture a screenshot on test failure
        // captureScreenshot(result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Code to execute when a test is skipped
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Code to execute when a test fails but is within success percentage
    }

    @Override
    public void onStart(ITestContext context) {
        // Code to execute before any test method in the context is run
    }

    @Override
    public void onFinish(ITestContext context) {
        // Code to execute after all test methods in the context have run
    }
}
