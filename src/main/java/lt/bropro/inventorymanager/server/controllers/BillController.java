package lt.bropro.inventorymanager.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.bropro.inventorymanager.server.database.BillRepository;
import lt.bropro.inventorymanager.server.database.StoredItemsRepository;
import lt.bropro.inventorymanager.server.exceptions.BillNotFoundException;
import lt.bropro.inventorymanager.server.exceptions.ItemNotFoundException;
import lt.bropro.inventorymanager.server.model.Bill;
import lt.bropro.inventorymanager.server.model.Role;
import lt.bropro.inventorymanager.server.model.StoredItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bropro/bills")
@Tag(name = "Bill Controller")
public class BillController {

    @Autowired
    private BillRepository billRepository;
    
    @Operation(
            summary = "Get all bills.",
            description = "Returns all bills"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all bills.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bill.class))}),
            @ApiResponse(responseCode = "404", description = "Bill not found.",
                    content = @Content)
    })
    @GetMapping
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }
    
    @Operation(
            summary = "Get a bill by ID",
            description = "Retrieve a bill by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the bills",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bill.class))}),
            @ApiResponse(responseCode = "404", description = "Bill not found",
                    content = @Content)
    })
    @GetMapping("/{billId}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            return ResponseEntity.ok().body(bill);
        } else {
            throw new BillNotFoundException(billId);
        }
    }

    @Operation(
            summary = "Add a new bill",
            description = "Add a bill to an event"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bill added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bill.class))})
    })
    @PostMapping
    public ResponseEntity<Bill> addBillToEvent(@RequestBody Bill bill) {
        Bill savedBill = billRepository.save(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBill);
    }

    @Operation(
            summary = "Update a bill",
            description = "Update an existing bill based on the provided bill ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bill updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bill.class))}),
            @ApiResponse(responseCode = "404", description = "Bill not found",
                    content = @Content)
    })
    @PutMapping("/{billId}")
    public ResponseEntity<Bill> updateBill(@PathVariable Long billId, @RequestBody Bill billDetails) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            bill.setBillNumber(billDetails.getBillNumber());
            bill.setPrice(billDetails.getPrice());
            bill.setAdditionalCosts(billDetails.getAdditionalCosts());
            Bill updatedBill = billRepository.save(bill);
            return ResponseEntity.ok(updatedBill);
        } else {
            throw new BillNotFoundException(billId);
        }
    }

    @Operation(
            summary = "Delete a bill",
            description = "Delete an existing bill based on the provided bill ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bill was deleted.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Bill.class))})
    })
    @DeleteMapping("/{billId}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long billId) {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isPresent()) {
            billRepository.delete(optionalBill.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ItemNotFoundException(billId);
        }
    }
}