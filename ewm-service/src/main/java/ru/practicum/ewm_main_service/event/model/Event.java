package ru.practicum.ewm_main_service.event.model;

import lombok.*;
import ru.practicum.ewm_main_service.category.Category;
import ru.practicum.ewm_main_service.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "annotation", nullable = false)
    private String annotation;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status state;
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;
    @Column(name = "paid", nullable = false)
    private boolean paid;
    @Column(name = "participantLimit")
    private long participantLimit;
    @Column(name = "requestModeration")
    private boolean requestModeration = true;
    @Column(name = "confirmed_requests")
    private long confirmedRequests;
    @Column(name = "views")
    private long views;
}
