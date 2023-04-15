package ru.tinkoff.edu.scrapper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(Subscription.SubscriptionId.class)
@Table(name = "subscription")
public class Subscription {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubscriptionId implements Serializable {
        private Long linkId;
        private Long chatId;
    }

    @Id
    @Column(name = "link_id", nullable = false)
    private Long linkId;

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "link_id", insertable = false, updatable = false)
    private Link link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;
}
