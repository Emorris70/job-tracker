package com.tracker.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ApplicationStatusHistory}.
 */
class ApplicationStatusHistoryTest {

    /** No-arg constructor should leave job and status null. */
    @Test
    void defaultConstructor_jobAndStatusAreNull() {
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        assertNull(history.getJob());
        assertNull(history.getStatus());
    }

    /** No-arg constructor should set enteredAt to approximately now. */
    @Test
    void defaultConstructor_enteredAtDefaultsToNow() {
        LocalDateTime before = LocalDateTime.now();
        ApplicationStatusHistory history = new ApplicationStatusHistory();
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(history.getEnteredAt());
        assertFalse(history.getEnteredAt().isBefore(before));
        assertFalse(history.getEnteredAt().isAfter(after));
    }

    /** Full constructor should store the job and status correctly. */
    @Test
    void fullConstructor_setsJobAndStatus() {
        Job job = new Job();
        ApplicationStatusHistory history = new ApplicationStatusHistory(job, "Screening");

        assertEquals(job, history.getJob());
        assertEquals("Screening", history.getStatus());
    }

    /** Full constructor should still initialise enteredAt to approximately now. */
    @Test
    void fullConstructor_enteredAtDefaultsToNow() {
        LocalDateTime before = LocalDateTime.now();
        ApplicationStatusHistory history = new ApplicationStatusHistory(new Job(), "Interview");
        LocalDateTime after = LocalDateTime.now();

        assertFalse(history.getEnteredAt().isBefore(before));
        assertFalse(history.getEnteredAt().isAfter(after));
    }

    /** Setter should overwrite the status value. */
    @Test
    void setStatus_updatesStatus() {
        ApplicationStatusHistory history = new ApplicationStatusHistory(new Job(), "Applied");
        history.setStatus("Offer");
        assertEquals("Offer", history.getStatus());
    }
}
