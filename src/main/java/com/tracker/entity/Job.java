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

    /**
     * Instantiates a new Job object.
     */
    public Job() {
    }

    /**
     * Instantiates a new Job object.
     * And initializes the unique identifier.
     *
     * @param user the user who applied for the job.
     * @param companyName the company name.
     * @param jobTitle the job title.
     * @param location the location.
     * @param salaryRange the salary range.
     * @param status the status.
     * @param jobUrl the job url.
     */
    public Job( User user, String companyName, String jobTitle, String location,
               String salaryRange, String status, String jobUrl,
                String description, LocalDate dateApplied)
    {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return id == job.id && Objects.equals(companyName, job.companyName)
                && Objects.equals(jobTitle, job.jobTitle)
                && Objects.equals(location, job.location)
                && Objects.equals(salaryRange, job.salaryRange)
                && Objects.equals(status, job.status)
                && Objects.equals(jobUrl, job.jobUrl)
                && Objects.equals(dateApplied, job.dateApplied)
                && Objects.equals(description, job.description)
                && Objects.equals(user, job.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, jobTitle, location,
                salaryRange, status, jobUrl, dateApplied, description, user);
    }
}
