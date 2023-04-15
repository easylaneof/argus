package ru.tinkoff.edu.scrapper.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.tinkoff.edu.scrapper.repository.jpa.UriAttributeConverter;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "link")
public class Link {
    @Id
    @GeneratedValue(
            generator = "link_id_seq",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(name = "link_id_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "url", unique = true, nullable = false)
    @Convert(converter = UriAttributeConverter.class)
    private URI url;

    @Column(name = "last_checked_at")
    private OffsetDateTime lastCheckedAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "updates_count")
    private Integer updatesCount;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id")
    @Builder.Default
    private List<Subscription> subscriptions = new ArrayList<>();

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
        subscription.setLink(this);
    }
}
