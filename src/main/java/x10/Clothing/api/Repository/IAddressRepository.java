package x10.Clothing.api.Repository;

import x10.Clothing.api.common.domain.entities.AddressEntity;

import java.util.List;
import java.util.Optional;

public interface IAddressRepository {
    AddressEntity save(AddressEntity address);
    Optional<AddressEntity> findById(String id);
    List<AddressEntity> findByUserId(String userId);
    void deleteById(String id);
}
