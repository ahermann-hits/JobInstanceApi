package de.hits.jobinstance;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.hits.jobinstance.service.JobInstanceService;

/**
 * 
 * @author André Hermann
 * @since 10.02.2018
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { JobInstanceApi.class }, properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
public abstract class JobInstanceApiTest {

	@Mock
	protected JobInstanceService jobService;

	@Autowired
	protected MockMvc mockMvc;
}