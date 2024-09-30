package com.example.bookservice.command.aggregate;

import com.example.bookservice.command.command.CreateBookCommand;
import com.example.bookservice.command.command.DeleteBookCommand;
import com.example.bookservice.command.command.UpdateBookCommand;
import com.example.bookservice.command.event.BookCreatedEvent;
import com.example.bookservice.command.event.BookDeletedEvent;
import com.example.bookservice.command.event.BookUpdatedEvent;
import lombok.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.*;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@Aggregate
public class BookAggregate {

//    liên kết id từ createBookCommand với id của book
    @AggregateIdentifier
    private String id;
    private String name;
    private String author;
    private Boolean isReady;

    @CommandHandler
    public BookAggregate(CreateBookCommand command) {
        BookCreatedEvent event = new BookCreatedEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateBookCommand command) {
        BookUpdatedEvent event = new BookUpdatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DeleteBookCommand command) {
        BookDeletedEvent bookDeletedEvent = new BookDeletedEvent();
        BeanUtils.copyProperties(command, bookDeletedEvent);
        AggregateLifecycle.apply(bookDeletedEvent);
    }

    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookUpdatedEvent event){
        this.id = event.getId();
        this.name = event.getName();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.id = event.getId();
    }
}