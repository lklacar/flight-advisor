package com.htecgroup.flightadvisor.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "city", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "country"}))
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class City extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(length = 100, nullable = false)
    private String country;

    @NotNull
    @Size(min = 1, max = 2000)
    @Column(length = 100, nullable = false)
    private String description;

    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "city", cascade = CascadeType.REMOVE)
    private Set<Comment> comments;
}
