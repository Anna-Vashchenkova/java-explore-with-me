package ru.practicum.ewm_main_service.comment;

import lombok.*;
import ru.practicum.ewm_main_service.event.model.Event;
import ru.practicum.ewm_main_service.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text", nullable = false)
    private String text;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    @Column(name = "comment_created_on", nullable = false)
    private LocalDateTime createdOn;
}
