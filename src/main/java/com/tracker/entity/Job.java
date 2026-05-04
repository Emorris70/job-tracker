package com.tracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a job application persisted in the {@code applications} table.
 * <p>
 * {@code status} defaults to {@code "Applied"} and {@code dateApplied} defaults
 * to the current date when the no-arg constructor is used.
 * </p>
 *
 * @author EmileM
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "applications")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "location")
    private String location;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "status")
    private String status = "Applied";

    @Column(name = "job_url", columnDefinition = "TEXT")
    private String jobUrl;

    @Column(name = "date_applied")
    private LocalDate dateApplied = LocalDate.now();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApplicationStatusHistory> statusHistory = new ArrayList<>();

    /**
     * @param user the user who applied.
     * @param companyName the company name.
     * @param jobTitle the job title.
     * @param location the job location.
     * @param salaryRange the salary range.
     * @param status the initial application status.
     * @param jobUrl the URL of the job posting.
     * @param description the job description.
     * @param dateApplied the date the application was submitted.
     */
    public Job(User user, String companyName, String jobTitle, String location,
               String salaryRange, String status, String jobUrl,
               String description, LocalDate dateApplied) {
        this.user = user;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.location = location;
        this.salaryRange = salaryRange;
        this.status = status;
        this.jobUrl = jobUrl;
        this.description = description;
        this.dateApplied = dateApplied;
    }
}
