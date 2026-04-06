package com.tracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a job application.
 *
 * @author EmileM
 */
@Entity
@Table(name = "applications")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId; // Unique ID from AWS Cognito

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

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "date_applied")
    private LocalDate dateApplied = LocalDate.now();;

    /**
     * Instantiates a new Job object.
     */
    public Job() {
    }

    /**
     * Overloaded constructor for quick initialization of core job details.
     * @param userId The unique identifier from Cognito.
     * @param companyName Name of the hiring company.
     * @param jobTitle Title of the position.
     */
    public Job(String userId, String companyName, String jobTitle) {
        this.userId = userId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobUrl() {
        return jobUrl;
    }

    public void setJobUrl(String jobUrl) {
        this.jobUrl = jobUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id) && Objects.equals(userId, job.userId)
                && Objects.equals(companyName, job.companyName)
                && Objects.equals(jobTitle, job.jobTitle)
                && Objects.equals(location, job.location)
                && Objects.equals(salaryRange, job.salaryRange)
                && Objects.equals(status, job.status) && Objects.equals(jobUrl, job.jobUrl)
                && Objects.equals(notes, job.notes)
                && Objects.equals(dateApplied, job.dateApplied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, companyName,
                jobTitle, location, salaryRange, status,
                jobUrl, notes, dateApplied, dateApplied);
    }
}
