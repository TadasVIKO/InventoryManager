package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing ItemCategory entities.
 *
 * Provides basic CRUD operations for ItemCategory entities by extending JpaRepository.
 */
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {
}
