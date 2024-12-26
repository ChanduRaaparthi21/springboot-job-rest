package com.chandu.controller;

import com.chandu.model.Job;
import com.chandu.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JobRestControllerTest {

    @Mock
    private JobService jobService;

    @InjectMocks
    private JobRestController jobRestController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobRestController).build();
    }

    @Test
    void testGetAllJobs() throws Exception {
        List<Job> jobs = Arrays.asList(
                new Job(1, "Developer", "Java Developer", 3, Arrays.asList("Java", "Spring")),
                new Job(2, "Tester", "QA Tester", 2, Arrays.asList("Selenium", "JUnit"))
        );

        when(jobService.getAllJobs()).thenReturn(jobs);

        mockMvc.perform(get("/jobPosts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].postProfile").value("Developer"));

        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    void testGetJob() throws Exception {
        Job job = new Job(1, "Developer", "Java Developer", 3, Arrays.asList("Java", "Spring"));

        when(jobService.getJob(1)).thenReturn(job);

        mockMvc.perform(get("/jobPost/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postProfile").value("Developer"))
                .andExpect(jsonPath("$.postDesc").value("Java Developer"));

        verify(jobService, times(1)).getJob(1);
    }

    
    //some error here
    @Test
    void testAddJob() throws Exception {
        Job job = new Job(3, "Manager", "Project Manager", 5, Arrays.asList("Agile", "Scrum"));

        // Use doNothing() for mocking void methods
        doNothing().when(jobService).addJob(any(Job.class));

        mockMvc.perform(post("/addJob")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"postId\":3,\"postProfile\":\"Manager\",\"postDesc\":\"Project Manager\",\"reqExperience\":5,\"postTechStack\":[\"Agile\",\"Scrum\"]}"))
                .andExpect(status().isOk());

        verify(jobService, times(1)).addJob(any(Job.class));
    }

    //some error here
    @Test
    void testUpdateJob() throws Exception {
        Job updatedJob = new Job(1, "Lead Developer", "Senior Java Developer", 7, Arrays.asList("Java", "Spring Boot"));

        // Use doNothing() for mocking void methods
        doNothing().when(jobService).updateJob(any(Job.class));

        mockMvc.perform(put("/jobPost")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"postId\":1,\"postProfile\":\"Lead Developer\",\"postDesc\":\"Senior Java Developer\",\"reqExperience\":7,\"postTechStack\":[\"Java\",\"Spring Boot\"]}"))
                .andExpect(status().isOk());

        verify(jobService, times(1)).updateJob(any(Job.class));
    }


    @Test
    void testDeleteJob() throws Exception {
        doNothing().when(jobService).deleteJob(1);

        mockMvc.perform(delete("/jobPost/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("deleted"));

        verify(jobService, times(1)).deleteJob(1);
    }

    @Test
    void testSearchByKeyword() throws Exception {
        List<Job> jobs = Arrays.asList(
                new Job(1, "Developer", "Java Developer", 3, Arrays.asList("Java", "Spring"))
        );

        when(jobService.search("Developer")).thenReturn(jobs);

        mockMvc.perform(get("/jobPosts/keyword/Developer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].postProfile").value("Developer"));

        verify(jobService, times(1)).search("Developer");
    }
}
