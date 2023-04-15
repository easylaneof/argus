package ru.tinkoff.edu.scrapper.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    private Long id;

    public Chat(Long id) {
        this.id = id;
    }
}
