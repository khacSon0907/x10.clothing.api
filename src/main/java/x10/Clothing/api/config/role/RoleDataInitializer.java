package x10.Clothing.api.config.role;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import x10.Clothing.api.Repository.IRoleRepository;
import x10.Clothing.api.common.domain.entities.user.RoleEntity;

import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class RoleDataInitializer implements CommandLineRunner {

    private final IRoleRepository roleRepository;

    @Override
    public void run(String... args) {

        List<RoleEntity> defaultRoles = List.of(
                RoleEntity.builder()
                        .code("USER")
                        .name("User")
                        .description("Default customer role")
                        .active(true)
                        .build()
        );

        defaultRoles.stream()
                .filter(role -> !roleRepository.existsByCode(role.getCode()))
                .forEach(roleRepository::save);
    }
}
