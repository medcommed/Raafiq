package ma.raafiq.repository;

import ma.raafiq.domain.Beneficiaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Beneficiaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long>, JpaSpecificationExecutor<Beneficiaire> {}
