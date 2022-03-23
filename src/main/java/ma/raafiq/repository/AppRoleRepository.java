package ma.raafiq.repository;

import ma.raafiq.domain.AppRole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long>, JpaSpecificationExecutor<AppRole> {}
