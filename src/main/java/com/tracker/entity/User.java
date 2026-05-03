package com.tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a registered user, identified by their AWS Cognito subject (sub).
 *
 * @author EmileM
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
     * @param sub the unique Cognito subject identifier for this user.
     */
    public User(String sub) {
        this.sub = sub;
    }

    /**
     * Adds a job to this user's list and sets the owning side of the relationship.
     *
     * @param job the job to add.
     */
    public void addJob(Job job) {
        this.jobs.add(job);
        job.setUser(this);
    }

    /**
     * Removes a job from this user's list and clears the owning side of the relationship.
     *
     * @param job the job to remove.
     */
    public void removeJob(Job job) {
        this.jobs.remove(job);
        job.setUser(null);
    }
}
