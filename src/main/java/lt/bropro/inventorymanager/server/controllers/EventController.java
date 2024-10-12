package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.*;
import lt.bropro.inventorymanager.server.database.EventRepository;
import lt.bropro.inventorymanager.server.exceptions.EmployeeNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.EventNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.RoleNotFoundException;
import lt.bropro.inventorymanager.server.model.*;
import lt.bropro.inventorymanager.server.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/events")
@Tag(name = "Event Controller")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StoredItemsRepository storedItemsRepository;

    @Autowired
    private BillRepository billRepository;

    @Operation(
            summary = "Get all events.",
            description = "Returns all events."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all events.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Event not found.",
                    content = @Content)
    })
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Operation(
            summary = "Get an event by ID",
            description = "Retrieves an event by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content)
    })
    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            return ResponseEntity.ok().body(event);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Operation(
            summary = "Add an event",
            description = "Add a new event"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))})
    })
    @PostMapping
    public ResponseEntity<Event> addEventToHotel(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @Operation(
            summary = "Update an event by ID",
            description = "Update an existing event based on the provided event ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Event not found",
                    content = @Content)
    })
    @PutMapping("/{eventId}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long eventId, @RequestBody Event eventDetails) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            event.setEventType(eventDetails.getEventType());
            event.setAddress(eventDetails.getAddress());
            event.setDate(eventDetails.getDate());
            event.setArrivalTime(eventDetails.getArrivalTime());
            event.setMeetupTime(eventDetails.getMeetupTime());
            event.setGuestTime(eventDetails.getGuestTime());
            event.setEndTime(eventDetails.getEndTime());
            event.setReadyTime(eventDetails.getReadyTime());
            event.setSoundCheckTime(eventDetails.getSoundCheckTime());
            event.setTitle(eventDetails.getTitle());

            Event updatedEvent = eventRepository.save(event);
            return ResponseEntity.ok(updatedEvent);
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Operation(
            summary = "Delete an event",
            description = "Delete an existing event based on the provided event ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Event was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))})
    })
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            eventRepository.delete(event);
            return ResponseEntity.noContent().build();
        } else {
            throw new EventNotFoundException(eventId);
        }
    }

    @Operation(
            summary = "Add or remove employees for events",
            description = "Adds or removes employees to/from an event based on the provided employee ID and event IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event's employees updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Employee or Event not found",
                    content = @Content)
    })
    @PutMapping("/{eventId}/employees")
    public ResponseEntity<Event> updateEventEmployees(
            @PathVariable Long eventId,
            @RequestParam List<Long> employeeId,
            @RequestParam(defaultValue = "false") boolean removeEmployees) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<Employee> employees = employeeRepository.findAllById(employeeId);
        if (employees.size() != employeeId.size()) {
            throw new EmployeeNotFoundException(null);
        }

        if (removeEmployees) {
            event.getEmployeeList().removeAll(employees);
        } else {
            event.getEmployeeList().addAll(employees);
        }

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(
            summary = "Add or remove items to/from events",
            description = "Adds or removes items to/from an event based on the provided item IDs and event ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event's item updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Item or Event not found",
                    content = @Content)
    })
    @PutMapping("/{eventId}/items")
    public ResponseEntity<Event> updateEventItems(
            @PathVariable Long eventId,
            @RequestParam List<Long> itemId,
            @RequestParam(defaultValue = "false") boolean removeItems) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<StoredItems> storedItems = storedItemsRepository.findAllById(itemId);
        if (storedItems.size() != storedItems.size()) {
            throw new EmployeeNotFoundException(null);
        }

        if (removeItems) {
            event.getStoredItemsList().removeAll(storedItems);
        } else {
            event.getStoredItemsList().addAll(storedItems);
        }

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @Operation(
            summary = "Add or remove bills to/from events",
            description = "Adds or removes bills to/from an event based on the provided bill IDs and event ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event's bill updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Event.class))}),
            @ApiResponse(responseCode = "404", description = "Bill or Event not found",
                    content = @Content)
    })
    @PutMapping("/{eventId}/bills")
    public ResponseEntity<Event> updateEventBills(
            @PathVariable Long eventId,
            @RequestParam List<Long> billId,
            @RequestParam(defaultValue = "false") boolean removeBills) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        List<Bill> storedBills = billRepository.findAllById(billId);
        if (storedBills.size() != storedBills.size()) {
            throw new EmployeeNotFoundException(null);
        }

        if (removeBills) {
            event.getBillList().removeAll(storedBills);
        } else {
            event.getBillList().addAll(storedBills);
        }

        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }
}