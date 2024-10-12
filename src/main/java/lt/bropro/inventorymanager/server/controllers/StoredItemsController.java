package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.ItemRepository;
import lt.bropro.inventorymanager.server.database.StoredItemsRepository;
import lt.bropro.inventorymanager.server.exceptions.EmployeeNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.ItemNotFoundException;
import lt.bropro.inventorymanager.server.model.Employee;
import lt.bropro.inventorymanager.server.model.Item;
import lt.bropro.inventorymanager.server.model.Role;
import lt.bropro.inventorymanager.server.model.StoredItems;
import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/stored-items")
@Tag(name = "Stored Items Controller")
public class StoredItemsController {

    @Autowired
    private StoredItemsRepository storedItemsRepository;
    
    @Operation(
            summary = "Get all stored items.",
            description = "Returns all stored items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all stored items.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoredItems.class))}),
            @ApiResponse(responseCode = "404", description = "Stored items not found.",
                    content = @Content)
    })
    @GetMapping
    public List<StoredItems> getAllItems() {
        return storedItemsRepository.findAll();
    }
    
    @Operation(
            summary = "Get a stored item by ID",
            description = "Retrieve a stored item by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the stored item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Stored item not found",
                    content = @Content)
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<StoredItems> getItemById(@PathVariable Long storedItemId) {
        Optional<StoredItems> optionalStoredItems = storedItemsRepository.findById(storedItemId);
        if (optionalStoredItems.isPresent()) {
            StoredItems item = optionalStoredItems.get();
            return ResponseEntity.ok().body(item);
        } else {
            throw new ItemNotFoundException(storedItemId);
        }
    }

    @Operation(
            summary = "Find an item by its availability",
            description = "Retrieve all stored items that are available"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoredItems.class))}),
            @ApiResponse(responseCode = "404", description = "Items not found.",
                    content = @Content)
    })
    @GetMapping("/find")
    public ResponseEntity<List<StoredItems>> getItemsByAvailability(
            @RequestParam(value = "availability") Boolean available) {

        // Fetch all items based on the availability flag
        List<StoredItems> storedItemsList = storedItemsRepository.findByAvailability(available);

        if (!storedItemsList.isEmpty()) {
            // If items are found, return them
            return ResponseEntity.ok().body(storedItemsList);
        } else {
            // If no items are found, throw a custom exception or return an empty list
            throw new ItemNotFoundException(null);
        }
    }

    @Operation(
            summary = "Add a new stored item",
            description = "Add a new item to the storage"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoredItems.class))})
    })
    @PostMapping
    public ResponseEntity<StoredItems> addItemToStorage(@RequestBody StoredItems item) {
        StoredItems savedItem = storedItemsRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @Operation(
            summary = "Update a stored item",
            description = "Update an existing stored item based on the provided item ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoredItems.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content)
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<StoredItems> updateItem(@PathVariable Long itemId, @RequestBody StoredItems itemDetails) {
        Optional<StoredItems> optionalItem = storedItemsRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            StoredItems item = optionalItem.get();
            item.setItem(itemDetails.getItem());
            item.setAvailability(itemDetails.getAvailability());
            item.setRentPrice(itemDetails.getRentPrice());
            StoredItems updatedItem = storedItemsRepository.save(item);
            return ResponseEntity.ok(updatedItem);
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }

    @Operation(
            summary = "Delete a stored item",
            description = "Delete an existing stored item based on the provided item ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoredItems.class))})
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        Optional<StoredItems> optionalItem = storedItemsRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            storedItemsRepository.delete(optionalItem.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }
}