package com.example.bookservice.command.event;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreatedEvent {
    private String id;
    private String name;
    private String author;
    private Boolean isReady;
}