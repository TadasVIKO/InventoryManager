package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Event entities.
 *
 * Provides basic CRUD operations for Event entities by extending JpaRepository.
 */
public interface EventRepository extends JpaRepository<Event, Long> {
}
