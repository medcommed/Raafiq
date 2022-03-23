package ma.raafiq.repository;

import ma.raafiq.domain.Famille;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Famille entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilleRepository extends JpaRepository<Famille, Long>, JpaSpecificationExecutor<Famille> {}
