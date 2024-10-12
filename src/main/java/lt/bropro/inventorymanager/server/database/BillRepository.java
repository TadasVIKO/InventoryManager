package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Bill entities.
 *
 * Provides basic CRUD operations for Bill entities by extending JpaRepository.
 */
public interface BillRepository extends JpaRepository<Bill, Long> {
}
