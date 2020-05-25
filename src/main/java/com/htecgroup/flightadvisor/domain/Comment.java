package com.htecgroup.flightadvisor.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comment")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Comment extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private ApplicationUser author;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(length = 100, nullable = false)
    private String text;
}
