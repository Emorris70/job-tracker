package com.tracker_test.entity;

import com.tracker.entity.Job;
import com.tracker.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link User}.
 */
class UserTest {

    /** Constructor should store the Cognito sub. */
    @Test
    void constructor_setsSub() {
        User user = new User("cognito-sub-abc");
        assertEquals("cognito-sub-abc", user.getSub());
    }

    /** No-arg constructor should initialise jobs as an empty list, not null. */
    @Test
    void defaultConstructor_jobsIsEmptyList() {
        User user = new User();
        assertNotNull(user.getJobs());
        assertTrue(user.getJobs().isEmpty());
    }

    /** addJob should append the job to the user's list and set the owning side. */
    @Test
    void addJob_addsJobAndSetsUserReference() {
        User user = new User("sub-001");
        Job job = new Job();

        user.addJob(job);

        assertTrue(user.getJobs().contains(job));
        assertEquals(user, job.getUser());
    }

    /** addJob on multiple jobs should keep all of them in the list. */
    @Test
    void addJob_multipleJobs_allPresent() {
        User user = new User("sub-001");
        Job job1 = new Job();
        Job job2 = new Job();

        user.addJob(job1);
        user.addJob(job2);

        assertEquals(2, user.getJobs().size());
    }

    /** removeJob should remove the job from the list and clear the owning side. */
    @Test
    void removeJob_removesJobAndClearsUserReference() {
        User user = new User("sub-001");
        Job job = new Job();
        user.addJob(job);

        user.removeJob(job);

        assertFalse(user.getJobs().contains(job));
        assertNull(job.getUser());
    }

    /** Removing a job that was never added should leave the list unchanged. */
    @Test
    void removeJob_jobNotInList_listUnchanged() {
        User user = new User("sub-001");
        Job existing = new Job();
        Job notAdded = new Job();
        user.addJob(existing);

        user.removeJob(notAdded);

        assertEquals(1, user.getJobs().size());
        assertTrue(user.getJobs().contains(existing));
    }
}
