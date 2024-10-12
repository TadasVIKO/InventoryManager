package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.EmployeeRepository;
import lt.bropro.inventorymanager.server.database.RoleRepository;
import lt.bropro.inventorymanager.server.exceptions.EmployeeNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.RoleNotFoundException;
import lt.bropro.inventorymanager.server.model.Employee;
import lt.bropro.inventorymanager.server.model.EmployeeChange;
import lt.bropro.inventorymanager.server.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/employees")
@Tag(name = "Employee Controller")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Get all employees.",
            description = "Returns all employees."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all employees.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employees not found.",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @Operation(
            summary = "Get an employee by ID",
            description = "Retrieves an employee by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return ResponseEntity.ok().body(employee);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Operation(
            summary = "Find an employee by email",
            description = "Retrieve an employee by their email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found.",
                    content = @Content)
    })
    @GetMapping("/findEmail")
    public ResponseEntity<Employee> getEmployeeByEmail(
            @RequestParam(value = "email") String email) {

        System.out.println("Received request for email: " + email);

        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email).stream().findFirst();
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return ResponseEntity.ok().body(employee);
        } else {
            throw new EmployeeNotFoundException(null);
        }
    }

    @Operation(
            summary = "Find an employee by first and last names",
            description = "Retrieve an employee by their first and last names"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found.",
                    content = @Content)
    })
    @GetMapping("/find")
    public ResponseEntity<Employee> getEmployeeByName(
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "lastName") String lastName) {

        Optional<Employee> optionalEmployee = employeeRepository.findAll().stream()
                .filter(staff -> staff.getFirstName().equalsIgnoreCase(firstName) &&
                        staff.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return ResponseEntity.ok().body(employee);
        } else {
            throw new EmployeeNotFoundException(null);
        }
    }

    @Operation(
            summary = "Add an employee",
            description = "Add a new employee"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))})
    })
    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        String hashedPassword = passwordEncoder.encode(employee.getPassword());
        employee.setPassword(hashedPassword);

        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok().body(savedEmployee);
    }

    @Operation(
            summary = "Update an employee by ID",
            description = "Update an existing employee based on the provided employee ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @PutMapping("/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setFirstName(employeeDetails.getFirstName());
            employee.setLastName(employeeDetails.getLastName());
            employee.setAddress1(employeeDetails.getAddress1());
            employee.setAddress2(employeeDetails.getAddress2());
            employee.setEmail(employeeDetails.getEmail());
            employee.setMobilePhone(employeeDetails.getMobilePhone());

            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Operation(
            summary = "Update an employee password by ID",
            description = "Update an existing employee password based on the provided employee ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content)
    })
    @PutMapping("/{employeeId}/pass")
    public ResponseEntity<Employee> updateEmployeePassword(@PathVariable Long employeeId, @RequestBody EmployeeChange employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();

            if (passwordEncoder.matches(employeeDetails.getCurrentPassword(), employee.getPassword())){
                if (employeeDetails.getNewPassword().equals(employeeDetails.getConfirmNewPassword())){
                    String hashedPassword = passwordEncoder.encode(employeeDetails.getNewPassword());
                    employee.setPassword(hashedPassword);

                    Employee updatedEmployee = employeeRepository.save(employee);
                    return ResponseEntity.ok(updatedEmployee);
                } else {
                    throw new RuntimeException("2");
                }
            } else {
                throw new RuntimeException("1");
            }
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Operation(
            summary = "Delete an employee",
            description = "Delete an existing employee based on the provided staff member ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employee was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))})
    })
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employeeRepository.delete(employee);
            return ResponseEntity.noContent().build();
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @Operation(
            summary = "Add or remove roles for an employee",
            description = "Adds or removes roles to/from an employee based on the provided employee ID and role IDs"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee roles updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee or Role not found",
                    content = @Content)
    })
    @PutMapping("/{employeeId}/roles")
    public ResponseEntity<Employee> updateEmployeeRoles(
            @PathVariable Long employeeId,
            @RequestParam List<Long> roleIds,
            @RequestParam(defaultValue = "false") boolean removeRoles) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.size() != roleIds.size()) {
            throw new RoleNotFoundException(null);
        }

        if (removeRoles) {
            employee.getRoleList().removeAll(roles);
        } else {
            employee.getRoleList().addAll(roles);
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @Operation(
            summary = "Get an employee's roles by ID",
            description = "Retrieves an employee's roles by their ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee roles found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee roles not found",
                    content = @Content)
    })
    @GetMapping("/{employeeId}/roles")
    public ResponseEntity<List<Role>> getEmployeesRolesByID(@PathVariable Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            List<Role> employeeRoles = optionalEmployee.get().getRoleList();
            return ResponseEntity.ok().body(employeeRoles);
        } else {
            throw new EmployeeNotFoundException(null);
        }
    }

    @Operation(
            summary = "Get an employee's roles by email",
            description = "Retrieves an employee's roles by their email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee roles found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Employee.class))}),
            @ApiResponse(responseCode = "404", description = "Employee roles not found",
                    content = @Content)
    })
    @GetMapping("/roles/find")
    public ResponseEntity<List<Role>> getEmployeesRolesByEmail(@RequestParam(value = "email") String email) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
        if (optionalEmployee.isPresent()) {
            List<Role> employeeRoles = optionalEmployee.get().getRoleList();
            return ResponseEntity.ok().body(employeeRoles);
        } else {
            throw new EmployeeNotFoundException(null);
        }
    }
}