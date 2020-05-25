package com.htecgroup.flightadvisor.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.htecgroup.flightadvisor.config.Constants;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "application_user")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ApplicationUser extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "application_user_authority", joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Comment> comments = new HashSet<>();
}
