package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.EventTypeRepository;
import lt.bropro.inventorymanager.server.exceptions.EventTypeNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.RoleNotFoundException;
import lt.bropro.inventorymanager.server.model.EventType;
import lt.bropro.inventorymanager.server.model.Item;
import lt.bropro.inventorymanager.server.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/event-types")
@Tag(name = "Event Type Controller")
public class EventTypeController {

    @Autowired
    private EventTypeRepository eventTypeRepository;
    
    @Operation(
            summary = "Get all event types.",
            description = "Returns all event types"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all types.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "404", description = "Event types not found.",
                    content = @Content)
    })
    @GetMapping
    public List<EventType> getAllEventTypes() {
        return eventTypeRepository.findAll();
    }
    
    @Operation(
            summary = "Get an event type by ID",
            description = "Retrieve an event type by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the event type",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventType.class))}),
            @ApiResponse(responseCode = "404", description = "Event type not found",
                    content = @Content)
    })
    @GetMapping("/{eventTypeId}")
    public ResponseEntity<EventType> getItemById(@PathVariable Long eventTypeId) {
        Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
        if (optionalEventType.isPresent()) {
            EventType eventType = optionalEventType.get();
            return ResponseEntity.ok().body(eventType);
        } else {
            throw new EventTypeNotFoundException(eventTypeId);
        }
    }

    @Operation(
            summary = "Add a new event type",
            description = "Add a new event type to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event type added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventType.class))})
    })
    @PostMapping
    public ResponseEntity<EventType> addEventType(@RequestBody EventType eventType) {
        EventType savedEventType = eventTypeRepository.save(eventType);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEventType);
    }

    @Operation(
            summary = "Update an event type",
            description = "Update an existing event type based on the provided event type ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event type updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Event type not found",
                    content = @Content)
    })
    @PutMapping("/{eventTypeId}")
    public ResponseEntity<EventType> updateEventType(@PathVariable Long eventTypeId, @RequestBody EventType eventTypeDetails) {
        Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
        if (optionalEventType.isPresent()) {
            EventType eventType = optionalEventType.get();
            eventType.setName(eventTypeDetails.getName());
            eventType.setDescription(eventTypeDetails.getDescription());
            EventType updatedItem = eventTypeRepository.save(eventType);
            return ResponseEntity.ok(updatedItem);
        } else {
            throw new EventTypeNotFoundException(eventTypeId);
        }
    }

    @Operation(
            summary = "Delete an event type",
            description = "Delete an existing event type based on the provided event type ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event type was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventType.class))})
    })
    @DeleteMapping("/{eventTypeId}")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long eventTypeId) {
        Optional<EventType> optionalEventType = eventTypeRepository.findById(eventTypeId);
        if (optionalEventType.isPresent()) {
            eventTypeRepository.delete(optionalEventType.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new RoleNotFoundException(eventTypeId);
        }
    }
}