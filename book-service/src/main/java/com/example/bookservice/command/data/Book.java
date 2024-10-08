package com.example.bookservice.command.data;

import jakarta.persistence.*;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    private String id;
    private String name;
    private String author;
    private Boolean isReady;
}
