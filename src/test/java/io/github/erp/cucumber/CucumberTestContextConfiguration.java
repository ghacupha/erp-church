package io.github.erp.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import io.github.erp.IntegrationTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
