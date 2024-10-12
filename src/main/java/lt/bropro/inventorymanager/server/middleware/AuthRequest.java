package lt.bropro.inventorymanager.server.middleware;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.bropro.inventorymanager.server.model.Role;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    private String email;
    private String password;

}
