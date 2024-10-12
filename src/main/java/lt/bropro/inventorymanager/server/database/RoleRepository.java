package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Role entities.
 *
 * Provides basic CRUD operations for Role entities by extending JpaRepository.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
