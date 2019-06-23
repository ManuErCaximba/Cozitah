package repositories;

import domain.Audit;
import domain.Cozitah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CozitahRepository extends JpaRepository<Cozitah, Integer> {

    @Query("select c from Cozitah c where c.audit.auditor.id=?1 and c.isFinal=FALSE and c.audit.id=?2")
    Collection<Cozitah> getDraftCozitahsByAuditorOf(int auditorId, int auditId);

    @Query("select a from Audit a where a.auditor.id = ?1")
    Collection<Audit> getAuditsByAuditor(int auditorId);

    @Query("select c from Cozitah c where c.isFinal=TRUE and c.audit.id=?1")
    Collection<Cozitah> getFinalCozitahOf(int auditId);

    @Query("select c from Cozitah c where c.audit.id=?1")
    Collection<Cozitah> getCozitahsByAudit(int auditId);
}
