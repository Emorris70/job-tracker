package com.tracker.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private int id;

    @Column(name = "cognito_sub", unique = true, nullable = false)
    private String sub;

    @Column(name = "created_at",
            insertable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();

    /**
     * Instantiates a new User object.
     */
    public User() {}

    /**
     * Instantiates a new User object.
     * And initializes the unique identifier.
     *
     * @param sub The unique identifier from Cognito.
     */
    public User(String sub) {
        this.sub = sub;
    }

    /**
     * Adds a job to the list of jobs.
     * @param job the job to add.
     */
    public void addJob(Job job) {
        this.jobs.add(job);
        job.setUser(this);
    }


    /**
     * Removes a job from the list of jobs.
     * @param job the job to remove.
     */
    public void removeJob(Job job) {
        this.jobs.remove(job);
        job.setUser(null);
    }

    /**
     * Gets the list of jobs.
     * @return the list of jobs.
     */
    public List<Job> getJobs() {
        return jobs;
    }

    /**
     * Sets the list of jobs.
     * @param jobs the list of jobs.
     */
    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    /**
     * Gets the identifier from the database.
     * @return the unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier from the database.
     * @param id the unique identifier.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the unique identifier from Cognito.
     * @return the cognito unique identifier.
     */
    public String getSub() {
        return sub;
    }

    /**
     * Sets the unique identifier from Cognito.
     * @param sub the cognito unique identifier.
     */
    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(sub, user.sub);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sub);
    }
}
