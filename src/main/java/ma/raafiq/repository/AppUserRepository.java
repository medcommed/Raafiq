package ma.raafiq.repository;

import ma.raafiq.domain.AppUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {}
