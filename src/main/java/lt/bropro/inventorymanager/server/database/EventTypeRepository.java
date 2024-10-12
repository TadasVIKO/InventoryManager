package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing EventType entities.
 *
 * Provides basic CRUD operations for EventType entities by extending JpaRepository.
 */
public interface EventTypeRepository extends JpaRepository<EventType, Long> {
}
