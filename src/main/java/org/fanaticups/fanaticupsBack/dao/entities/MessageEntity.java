package org.fanaticups.fanaticupsBack.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "local_date", columnDefinition = "DATE")
    private LocalDate localDate;

    @ManyToOne
    private ChatEntity chat;

    @ManyToOne
    private UserEntity user;


}