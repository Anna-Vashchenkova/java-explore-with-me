package ru.practicum.statistics_service;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "hits")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String app;
    String uri;
    String ip;
    String timestamp;
}
