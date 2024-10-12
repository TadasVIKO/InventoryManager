package lt.bropro.inventorymanager.server.exceptions;

/**
 * Exception thrown when a hotel is not found.
 */
public class EventTypeNotFoundException extends RuntimeException {
    /**
     * Constructs a new HotelNotFoundException with the specified hotel ID.
     *
     * @param id the ID of the hotel that was not found
     */
    public EventTypeNotFoundException(Long id) {
        super("Could not find event type" + id);
    }
}
