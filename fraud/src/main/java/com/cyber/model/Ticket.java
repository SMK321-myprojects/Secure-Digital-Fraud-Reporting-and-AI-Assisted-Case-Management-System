package com.cyber.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String incidentType; 
    // Scam, Hacking, Fraud, Cyber Abuse

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.REPORTED;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime lastUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
//It maintains the relation between the user and ticket tables in the db.
    // getters and setters
    
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketMedia> mediaFiles = new ArrayList<>();

}
