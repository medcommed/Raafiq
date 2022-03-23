package ma.raafiq.repository;

import ma.raafiq.domain.MotifRefus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MotifRefus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MotifRefusRepository extends JpaRepository<MotifRefus, Long>, JpaSpecificationExecutor<MotifRefus> {}
