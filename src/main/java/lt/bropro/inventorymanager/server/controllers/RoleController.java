package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.RoleRepository;
import lt.bropro.inventorymanager.server.exceptions.RoleNotFoundException;
import lt.bropro.inventorymanager.server.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/roles")
@Tag(name = "Role Controller")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;
    
    @Operation(
            summary = "Get all roles.",
            description = "Returns all roles"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all roles.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Role not found.",
                    content = @Content)
    })
    @GetMapping
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    @Operation(
            summary = "Get a role by ID",
            description = "Retrieve a role by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the role",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)
    })
    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            return ResponseEntity.ok().body(role);
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }

    @Operation(
            summary = "Find a role by the name",
            description = "Retrieve a role by its name"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the role",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Role not found.",
                    content = @Content)
    })
    @GetMapping("/find")
    public ResponseEntity<Role> getRoleByName(@RequestParam(value = "name") String name) {
        Optional<Role> optionalRole = roleRepository.findAll().stream()
                .filter(staff -> staff.getName().equalsIgnoreCase(name))
                .findFirst();
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            return ResponseEntity.ok().body(role);
        } else {
            throw new RoleNotFoundException(null);
        }
    }

    @Operation(
            summary = "Add a new role",
            description = "Add a new role to the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))})
    })
    @PostMapping
    public ResponseEntity<Role> addRoleToHotel(@RequestBody Role role) {
        Role savedRole = roleRepository.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @Operation(
            summary = "Update a role",
            description = "Update an existing role based on the provided role ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))}),
            @ApiResponse(responseCode = "404", description = "Role not found",
                    content = @Content)
    })
    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRoleInHotel(@PathVariable Long roleId, @RequestBody Role roleDetails) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            role.setName(roleDetails.getName());
            role.setDescription(roleDetails.getDescription());
            Role updatedRole = roleRepository.save(role);
            return ResponseEntity.ok(updatedRole);
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }

    @Operation(
            summary = "Delete a role",
            description = "Delete an existing role based on the provided role ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Role.class))})
    })
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if (optionalRole.isPresent()) {
            roleRepository.delete(optionalRole.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new RoleNotFoundException(roleId);
        }
    }
}