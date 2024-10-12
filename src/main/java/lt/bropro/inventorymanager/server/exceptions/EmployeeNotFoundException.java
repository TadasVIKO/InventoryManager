package lt.bropro.inventorymanager.server.exceptions;

/**
 * Exception thrown when a hotel is not found.
 */
public class EmployeeNotFoundException extends RuntimeException {
    /**
     * Constructs a new HotelNotFoundException with the specified hotel ID.
     *
     * @param id the ID of the hotel that was not found
     */
    public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
