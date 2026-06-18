package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.guest.GuestEntity;

import java.util.Optional;

public interface IGuestRepository {

    GuestEntity save(GuestEntity guest);

    Optional<GuestEntity> findByEmail(String email);
}
