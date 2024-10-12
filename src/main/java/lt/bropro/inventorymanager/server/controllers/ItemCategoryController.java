package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.ItemCategoryRepository;
import lt.bropro.inventorymanager.server.database.ItemRepository;
import lt.bropro.inventorymanager.server.exceptions.ItemCategoryNotFoundException;
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
@RequestMapping("/bropro/items-categories")
@Tag(name = "Item Category Controller")
public class ItemCategoryController {

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @Operation(
            summary = "Get all item categories.",
            description = "Returns all item categories"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all categories.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "404", description = "Categories not found.",
                    content = @Content)
    })
    @GetMapping
    public List<ItemCategory> getAllItemCategories() {
        return itemCategoryRepository.findAll();
    }
    
    @Operation(
            summary = "Get an item category by ID",
            description = "Retrieve an item category by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the item category",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemCategory.class))}),
            @ApiResponse(responseCode = "404", description = "Item category not found",
                    content = @Content)
    })
    @GetMapping("/{itemCategoryId}")
    public ResponseEntity<ItemCategory> getItemById(@PathVariable Long itemCategoryId) {
        Optional<ItemCategory> optionalItemCategory = itemCategoryRepository.findById(itemCategoryId);
        if (optionalItemCategory.isPresent()) {
            ItemCategory itemCategory = optionalItemCategory.get();
            return ResponseEntity.ok().body(itemCategory);
        } else {
            throw new ItemCategoryNotFoundException(itemCategoryId);
        }
    }

    @Operation(
            summary = "Add a new item category",
            description = "Add a new item category to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item category added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemCategory.class))})
    })
    @PostMapping
    public ResponseEntity<ItemCategory> addItemCategory(@RequestBody ItemCategory itemCategory) {
        ItemCategory savedItemCategory = itemCategoryRepository.save(itemCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItemCategory);
    }

    @Operation(
            summary = "Update an item category",
            description = "Update an existing item category based on the provided item category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item category updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Item category not found",
                    content = @Content)
    })
    @PutMapping("/{itemCategoryId}")
    public ResponseEntity<ItemCategory> updateItemCategory(@PathVariable Long itemCategoryId, @RequestBody ItemCategory itemCategoryDetails) {
        Optional<ItemCategory> optionalItemCategory = itemCategoryRepository.findById(itemCategoryId);
        if (optionalItemCategory.isPresent()) {
            ItemCategory itemCategory = optionalItemCategory.get();
            itemCategory.setName(itemCategoryDetails.getName());
            itemCategory.setDescription(itemCategoryDetails.getDescription());
            ItemCategory updatedItem = itemCategoryRepository.save(itemCategory);
            return ResponseEntity.ok(updatedItem);
        } else {
            throw new ItemCategoryNotFoundException(itemCategoryId);
        }
    }

    @Operation(
            summary = "Delete an item category",
            description = "Delete an existing item category based on the provided item category ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item category was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemCategory.class))})
    })
    @DeleteMapping("/{itemCategoryId}")
    public ResponseEntity<Void> deleteItemCategory(@PathVariable Long itemCategoryId) {
        Optional<ItemCategory> optionalItemCategory = itemCategoryRepository.findById(itemCategoryId);
        if (optionalItemCategory.isPresent()) {
            itemCategoryRepository.delete(optionalItemCategory.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new RoleNotFoundException(itemCategoryId);
        }
    }
}