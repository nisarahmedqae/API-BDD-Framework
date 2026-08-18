package com.nahmed.runners;

import com.nahmed.utils.RuntimeConfigResolver;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.CucumberOptions.SnippetType;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        plugin = {"html:reports/cucumber/bdd_report.html",
                "rerun:target/cucumber/rerun.txt",
                "com.nahmed.listeners.TestListener"
        },
        features = {"src/test/java/com/nahmed/features"},
        glue = {"com.nahmed.stepdefinitions",
                "com.nahmed.events"},
        monochrome = true,
        snippets = SnippetType.CAMELCASE,
        tags = "@add_place"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    // 1. Parallel Execution Control
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() { return super.scenarios(); }

    // 2. Thread Count Configuration
    static {
        System.setProperty("dataproviderthreadcount", RuntimeConfigResolver.resolveThreadCount("threads"));
    }

}