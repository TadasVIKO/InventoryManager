package lt.bropro.inventorymanager.server.database;

import lt.bropro.inventorymanager.server.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Employee entities.
 *
 * Provides basic CRUD operations for Employee entities by extending JpaRepository.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);
}
