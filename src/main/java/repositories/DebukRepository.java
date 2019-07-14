package repositories;

import domain.Audit;
import domain.Debuk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface DebukRepository extends JpaRepository<Debuk, Integer> {

    @Query("select c from Debuk c where c.company.id=?1 and c.isFinal=FALSE and c.audit.id=?2")
    Collection<Debuk> getDraftDebuksByCompanyOf(int companyId, int auditId);

    @Query("select a from Audit a where a.auditor.id = ?1")
    Collection<Audit> getAuditsByAuditor(int auditorId);

    @Query("select c from Debuk c where c.isFinal=TRUE and c.audit.id=?1")
    Collection<Debuk> getFinalDebukOf(int auditId);

    @Query("select c from Debuk c where c.audit.id=?1")
    Collection<Debuk> getDebuksByAudit(int auditId);
}
