package lt.bropro.inventorymanager.server.exceptions;

/**
 * Exception thrown when a hotel is not found.
 */
public class ItemCategoryNotFoundException extends RuntimeException {
    /**
     * Constructs a new HotelNotFoundException with the specified hotel ID.
     *
     * @param id the ID of the hotel that was not found
     */
    public ItemCategoryNotFoundException(Long id) {
        super("Could not find item category" + id);
    }
}
