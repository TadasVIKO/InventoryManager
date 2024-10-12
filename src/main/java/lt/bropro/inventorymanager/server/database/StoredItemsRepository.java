package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.StoredItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing StoredItems entities.
 *
 * Provides basic CRUD operations for StoredItems entities by extending JpaRepository.
 */
public interface StoredItemsRepository extends JpaRepository<StoredItems, Long> {
    List<StoredItems> findByAvailability(Boolean availability);
}
