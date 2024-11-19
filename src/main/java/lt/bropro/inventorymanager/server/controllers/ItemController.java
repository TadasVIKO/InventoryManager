package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.ItemCategoryRepository;
import lt.bropro.inventorymanager.server.database.ItemRepository;
import lt.bropro.inventorymanager.server.exceptions.ItemNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.RoleNotFoundException;
import lt.bropro.inventorymanager.server.model.Item;
import lt.bropro.inventorymanager.server.model.ItemCategory;
import lt.bropro.inventorymanager.server.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/items")
@Tag(name = "Item Controller")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @Operation(
            summary = "Get all items.",
            description = "Returns all items"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all items.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "404", description = "Items not found.",
                    content = @Content)
    })
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    
    @Operation(
            summary = "Get an item by ID",
            description = "Retrieve an item by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the item",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content)
    })
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            return ResponseEntity.ok().body(item);
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }

    @Operation(
            summary = "Add a new item",
            description = "Add a new item to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))})
    })
    @PostMapping
    public ResponseEntity<Item> addItem(@RequestBody Item item) {
        if (item.getItemCategory() != null && item.getItemCategory().getId() != 0) {
            ItemCategory existingCategory = item.getItemCategory();
            item.setItemCategory(existingCategory);
        } else {
            item.setItemCategory(null);
        }

        Item savedItem = itemRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }


    @Operation(
            summary = "Update an item",
            description = "Update an existing item based on the provided item ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content)
    })
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable Long itemId, @RequestBody Item itemDetails) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            item.setName(itemDetails.getName());
            item.setDescription(itemDetails.getDescription());

            // Fetch the category by its ID
            if (itemDetails.getItemCategory() != null) {
                Optional<ItemCategory> category = itemCategoryRepository.findById(itemDetails.getItemCategory().getId());
                category.ifPresent(item::setItemCategory);
            }

            Item updatedItem = itemRepository.save(item);
            return ResponseEntity.ok(updatedItem);
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }



    @Operation(
            summary = "Delete an item",
            description = "Delete an existing item based on the provided item ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))})
    })
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            Item noCategoryItem = optionalItem.get();
            noCategoryItem.setItemCategory(null);
            itemRepository.save(noCategoryItem);

            itemRepository.delete(optionalItem.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ItemNotFoundException(itemId);
        }
    }
}