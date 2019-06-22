package repositories;

import domain.Cozitah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CozitahRepository extends JpaRepository<Cozitah, Integer> {

    @Query("select c from Cozitah c where c.company.id=?1")
    Collection<Cozitah> getCozitahsByCompany(int companyId);
}
