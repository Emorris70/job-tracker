package com.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a single status transition in a job application's lifecycle.
 * Each record captures the status entered and when it was entered, enabling
 * duration tracking across the Applied → Screening → Interview → Offer pipeline.
 *
 * @author EmileM
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "application_status_history")
public class ApplicationStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "entered_at", nullable = false)
    private LocalDateTime enteredAt = LocalDateTime.now();

    /**
     * Creates a new history entry for the given job and status.
     *
     * @param job    the job application being tracked.
     * @param status the status entered at this transition.
     */
    public ApplicationStatusHistory(Job job, String status) {
        this.job = job;
        this.status = status;
    }
}
