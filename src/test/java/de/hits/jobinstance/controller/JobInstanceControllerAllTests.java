package de.hits.jobinstance.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.hits.jobinstance.JobInstanceApiTest;

/**
 * 
 * @author André Hermann
 * @since 09.02.2018
 * @version 1.0
 */
@RunWith(Suite.class)
@SuiteClasses({ JobInstanceJobControllerTest.class, JobInstanceStatusControllerTest.class,
		JobInstanceCounterControllerTest.class })
public class JobInstanceControllerAllTests extends JobInstanceApiTest {
}