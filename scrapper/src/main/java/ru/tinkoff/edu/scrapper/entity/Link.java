package ru.tinkoff.edu.scrapper.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
    private Long id;
    private URI url;
    private OffsetDateTime lastCheckedAt;
}
