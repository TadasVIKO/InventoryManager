package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Item entities.
 *
 * Provides basic CRUD operations for Item entities by extending JpaRepository.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
