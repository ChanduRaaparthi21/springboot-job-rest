package com.chandu.service;

import com.chandu.model.Job;
import com.chandu.repo.JobRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private List<Job> jobList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobList = new ArrayList<>(List.of(
                new Job(1, "Software Engineer", "Exciting opportunity for a skilled software engineer, API.", 3, List.of("Java", "Spring", "SQL")),
                new Job(2, "Data Scientist", "Join our data science team and work on cutting-edge projects, API.", 5, List.of("Python", "Machine Learning", "TensorFlow"))
        ));
    }

    @Test
    void testGetAllJobs() {
        when(jobRepository.findAll()).thenReturn(jobList);

        List<Job> result = jobService.getAllJobs();

        assertNotNull(result);
        assertEquals(jobList.size(), result.size());
        assertEquals(jobList, result);
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void testAddJob() {
        Job newJob = new Job(3, "Frontend Developer", "Create amazing user interfaces with our talented frontend team, API.", 2, List.of("JavaScript", "React", "CSS"));

        jobService.addJob(newJob);

        verify(jobRepository, times(1)).save(newJob);
    }

    @Test
    void testGetJob() {
        int postId = 1;
        when(jobRepository.findById(postId)).thenReturn(java.util.Optional.ofNullable(jobList.get(0)));

        Job result = jobService.getJob(postId);

        assertNotNull(result);
        assertEquals(jobList.get(0), result);
        verify(jobRepository, times(1)).findById(postId);
    }

    @Test
    void testUpdateJob() {
        Job updatedJob = new Job(1, "Senior Software Engineer", "Updated job description.", 5, List.of("Java", "Spring", "Microservices"));

        jobService.updateJob(updatedJob);

        verify(jobRepository, times(1)).save(updatedJob);
    }

    @Test
    void testDeleteJob() {
        int postId = 1;

        jobService.deleteJob(postId);

        verify(jobRepository, times(1)).deleteById(postId);
    }

    @Test
    void testLoadData() {
        doAnswer(invocation -> {
            List<Job> savedJobs = invocation.getArgument(0);
            jobList.addAll(savedJobs);
            return null;
        }).when(jobRepository).saveAll(anyList());

        jobService.loadData();

        verify(jobRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testSearch() {
        String keyword = "Engineer";
        when(jobRepository.findByPostProfileContainingOrPostDescContaining(keyword, keyword)).thenReturn(jobList);

        List<Job> result = jobService.search(keyword);

        assertNotNull(result);
        assertEquals(jobList.size(), result.size());
        verify(jobRepository, times(1)).findByPostProfileContainingOrPostDescContaining(keyword, keyword);
    }
}
