package org.onix.kurentosfu.test.checks;

import io.cosmosoftware.kite.entities.VideoQuality;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;
import io.cosmosoftware.kite.util.TestUtils;
import org.onix.kurentosfu.test.pages.MainPage;
import org.openqa.selenium.JavascriptExecutor;

public class VideoCheck extends TestCheck {

    private static final String VIDEO_CURRENT_TIME_SCRIPT = "return document.getElementById('remote-video').currentTime;";

    private final MainPage mainPage;
    private final int videoDurationInSeconds;
    private final int waitAroundInMilliseconds;

    public VideoCheck(final Runner runner, final int videoDurationInSeconds, final int waitAroundInMilliseconds) {
        super(runner);
        this.mainPage = new MainPage(runner);
        this.videoDurationInSeconds = videoDurationInSeconds;
        this.waitAroundInMilliseconds = waitAroundInMilliseconds;
    }

    @Override
    public String stepDescription() {
        return "VideoCheck checks that checkPass is true";
    }

    @Override
    protected void step() throws KiteTestException {
        waitForVideoToLoad();

        this.logger.info("Looking for video objects");

        try {
            if (this.mainPage.getVideoElements().size() != 2) {
                throw new KiteTestException("Unable to find video elements on the page", Status.FAILED);
            }

            final String videoLocalCheck = TestUtils.videoCheck(this.webDriver, 0);
            final String videoRemoteCheck = TestUtils.videoQualityCheck(this.webDriver, 1);

            if (!VideoQuality.VIDEO.toString().equalsIgnoreCase(videoLocalCheck)) {
                this.reporter.textAttachment(report, "Sent Video", videoLocalCheck, "plain");
                throw new KiteTestException("Local video is " + videoLocalCheck, Status.FAILED);
            }

            if (!VideoQuality.VIDEO.toString().equalsIgnoreCase(videoRemoteCheck)) {
                this.reporter.textAttachment(report, "Sent Video", videoRemoteCheck, "plain");
                throw new KiteTestException("Remote video is " + videoRemoteCheck, Status.FAILED);
            }
        } catch (KiteTestException kiteTestException) {
            throw kiteTestException;
        } catch (Exception exception) {
            throw new KiteTestException("Error looking for the video", Status.BROKEN, exception);
        }
    }

    private void waitForVideoToLoad() {
        TestUtils.waitAround(this.videoDurationInSeconds * 1000);

        while (this.getVideoCurrentTime() <= this.videoDurationInSeconds) {
            TestUtils.waitAround(this.waitAroundInMilliseconds);
        }
    }

    private double getVideoCurrentTime() {
        final Object currentTime = ((JavascriptExecutor) this.webDriver).executeScript(VIDEO_CURRENT_TIME_SCRIPT);

        return ((Number) currentTime).doubleValue();
    }

}
