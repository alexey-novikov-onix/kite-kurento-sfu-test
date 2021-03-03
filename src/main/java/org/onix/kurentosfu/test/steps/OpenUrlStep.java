package org.onix.kurentosfu.test.steps;

import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.steps.TestStep;
import org.onix.kurentosfu.test.pages.MainPage;

public class OpenUrlStep extends TestStep {

    private final MainPage mainPage;
    private final String url;

    public OpenUrlStep(final Runner runner, final String url) {
        super(runner);
        this.mainPage = new MainPage(runner);
        this.url = url;
    }

    @Override
    public String stepDescription() {
        return "Open " + url;
    }

    @Override
    protected void step() {
        this.mainPage.open(url);
        this.mainPage.enterUsername("user" + this.getClientID());
        this.mainPage.clickLogin();
        this.mainPage.clickJoin();
    }

}
