package org.onix.kurentosfu.test;

import org.onix.kurentosfu.test.checks.VideoCheck;
import org.onix.kurentosfu.test.steps.OpenUrlStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

public class KiteKurentoSfuTest extends KiteBaseTest {

    private String url;
    private int videoDurationInSeconds;
    private int waitAroundInMilliseconds;

    @Override
    protected void payloadHandling() {
        this.url = this.payload.getString("url");
        this.videoDurationInSeconds = this.payload.getInt("videoDurationInSeconds");
        this.waitAroundInMilliseconds = this.payload.getInt("waitAroundInMilliseconds");

        logger.info("Payload handling " + this.payload);
    }

    @Override
    public void populateTestSteps(final TestRunner runner) {
        runner.addStep(new OpenUrlStep(runner, this.url));
        runner.addStep(new VideoCheck(runner, this.videoDurationInSeconds, this.waitAroundInMilliseconds));
    }

}
