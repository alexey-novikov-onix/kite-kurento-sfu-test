package org.onix.kurentosfu.test.checks;

import io.cosmosoftware.kite.entities.VideoQuality;
import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;
import io.cosmosoftware.kite.util.TestUtils;
import org.onix.kurentosfu.test.pages.MainPage;
import org.openqa.selenium.JavascriptExecutor;

public class VideosCheck extends TestCheck {

    private final MainPage mainPage;
    private final int videoDurationInSeconds;
    private final int waitAroundInMilliseconds;
    private final int tupleSize;

    public VideosCheck(
            final Runner runner,
            final int videoDurationInSeconds,
            final int waitAroundInMilliseconds,
            final int tupleSize
    ) {
        super(runner);
        this.mainPage = new MainPage(runner);
        this.videoDurationInSeconds = videoDurationInSeconds;
        this.waitAroundInMilliseconds = waitAroundInMilliseconds;
        this.tupleSize = tupleSize;
    }

    @Override
    public String stepDescription() {
        return "VideoCheck checks that checkPass is true";
    }

    @Override
    protected void step() throws KiteTestException {
        TestUtils.waitAround(this.tupleSize * 3 * 1000);

        this.logger.info("Looking for video objects");

        try {
            if (this.mainPage.getVideoElements().size() != this.tupleSize) {
                throw new KiteTestException("Unable to find video elements on the page", Status.FAILED);
            }

            for (int index = 0; index < this.tupleSize; index++) {
                final String videoCheck = TestUtils.videoQualityCheck(this.webDriver, index);

                if (!VideoQuality.VIDEO.toString().equalsIgnoreCase(videoCheck)) {
                    this.reporter.textAttachment(this.report, "Sent Video", videoCheck, "plain");

                    throw new KiteTestException("Local video is " + videoCheck, Status.FAILED);
                }
            }
        } catch (KiteTestException kiteTestException) {
            throw kiteTestException;
        } catch (Exception exception) {
            throw new KiteTestException("Error looking for the video", Status.BROKEN, exception);
        }
    }

}
