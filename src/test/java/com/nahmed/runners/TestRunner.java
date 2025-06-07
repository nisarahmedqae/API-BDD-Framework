package com.nahmed.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {"pretty:reports/cucumber/cucumber.txt",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "html:reports/cucumber/report.html",
                "json:reports/cucumber/cucumber.json",
                "com.nahmed.utils.TestListener"
        },
        features = {"src/test/java/com/nahmed/features"},
        glue = {"com.nahmed.stepdefinitions",
                "com.nahmed.events"},
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        tags = "@createBooking"
)

public class TestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}