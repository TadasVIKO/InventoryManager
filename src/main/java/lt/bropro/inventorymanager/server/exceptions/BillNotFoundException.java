package lt.bropro.inventorymanager.server.exceptions;

/**
 * Exception thrown when a hotel is not found.
 */
public class BillNotFoundException extends RuntimeException {
    /**
     * Constructs a new HotelNotFoundException with the specified hotel ID.
     *
     * @param id the ID of the hotel that was not found
     */
    public BillNotFoundException(Long id) {
        super("Could not find bill " + id);
    }
}
