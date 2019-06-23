package services;

import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.Cozitah;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.CozitahRepository;

import java.util.Collection;
import java.util.Date;

@Service
@Transactional
public class CozitahService {

    //Managed repository
    @Autowired
    private CozitahRepository cozitahRepository;

    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private AuditorService auditorService;

    //Validator
    @Autowired
    private Validator validator;


    //Simple CRUD Methods
    public Cozitah create() {
        Assert.isTrue(this.actorService.getActorLogged()
                .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("AUDITOR"));
        Cozitah result;
        result = new Cozitah();
        result.setIsFinal(false);
        return result;
    }

    public Collection<Cozitah> findAll() {
        return this.cozitahRepository.findAll();
    }

    public Cozitah findOne(final Integer id) {
        return this.cozitahRepository.findOne(id);
    }

    public Cozitah saveAsDraft(Cozitah cozitah) {
        Assert.notNull(cozitah);
        Assert.isTrue(this.actorService.getActorLogged()
                .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("AUDITOR"));
        Auditor auditor= this.auditorService.findOne(this.actorService.getActorLogged().getId());

        this.assertDeCompany(cozitah, auditor);

        Assert.isTrue(!cozitah.getIsFinal());
        cozitah = this.cozitahRepository.save(cozitah);

        return cozitah;
    }

    public Cozitah saveAsFinal(Cozitah cozitah) {
        Assert.notNull(cozitah);
        Auditor auditor= this.auditorService.findOne(this.actorService.getActorLogged().getId());

        this.assertDeCompany(cozitah, auditor);

        cozitah.setMoment(new Date());
        cozitah.setIsFinal(true);
        cozitah = this.cozitahRepository.save(cozitah);

        return cozitah;
    }

    //Reconstruct
    public Cozitah reconstruct(final Cozitah cozitah, final BindingResult binding) {
        Cozitah result;

        if (cozitah.getId() != 0){
            result = this.cozitahRepository.findOne(cozitah.getId());
            cozitah.setVersion(result.getVersion());
        }

        result = cozitah;
        this.validator.validate(cozitah, binding);

        return result;
    }

    //Another methods

    public void delete(final Cozitah cozitah) {
        Assert.notNull(cozitah);
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());
        this.assertDeCompany(cozitah, auditor);
        Assert.isTrue(!cozitah.getIsFinal());
        this.cozitahRepository.delete(cozitah);
    }

    public void deleteF(final Cozitah cozitah) {
        this.cozitahRepository.delete(cozitah);
    }

    public Collection<Cozitah> getDraftCozitahsByAuditorOf(int id, int auditId){
        return this.cozitahRepository.getDraftCozitahsByAuditorOf(id, auditId);
    }
    public Collection<Audit> getAuditsByAuditor(int id) { return this.cozitahRepository.getAuditsByAuditor(id);}
    public Collection<Cozitah> getFinalCozitahOf(int auditId) { return this.cozitahRepository.getFinalCozitahOf(auditId);}
    public Collection<Cozitah> getCozitahByAudit(int auditId) { return this.cozitahRepository.getCozitahsByAudit(auditId);}

    public void assertDeCompany(Cozitah cozitah, Auditor auditor){
        if(cozitah.getId()!=0) {
            Assert.isTrue(this.actorService.getActorLogged()
                    .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("AUDITOR"));
            Assert.isTrue(auditor.equals(cozitah.getAudit().getAuditor()));
        }
    }
}
