package ma.raafiq.repository;

import ma.raafiq.domain.Professionnel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Professionnel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfessionnelRepository extends JpaRepository<Professionnel, Long>, JpaSpecificationExecutor<Professionnel> {}
