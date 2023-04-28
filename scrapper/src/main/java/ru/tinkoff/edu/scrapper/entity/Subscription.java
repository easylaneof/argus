package ru.tinkoff.edu.scrapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(Subscription.SubscriptionId.class)
@Table(name = "subscription")
public class Subscription {
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

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SubscriptionId implements Serializable {
        private Long linkId;
        private Long chatId;
    }
}
