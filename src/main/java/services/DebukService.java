package services;

import domain.Audit;
import domain.Auditor;
import domain.Company;
import domain.Debuk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import repositories.DebukRepository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class DebukService {

    //Managed repository
    @Autowired
    private DebukRepository debukRepository;

    //Services
    @Autowired
    private ActorService actorService;
    @Autowired
    private AuditorService auditorService;
    @Autowired
    private CompanyService companyService;

    //Validator
    @Autowired
    private Validator validator;


    //Simple CRUD Methods
    public Debuk create() {
        Assert.isTrue(this.actorService.getActorLogged()
                .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
        Debuk result;
        result = new Debuk();
        result.setIsFinal(false);
        return result;
    }

    public Collection<Debuk> findAll() {
        return this.debukRepository.findAll();
    }

    public Debuk findOne(final Integer id) {
        return this.debukRepository.findOne(id);
    }

    public Debuk saveAsDraft(Debuk debuk) {
        Assert.notNull(debuk);
        Assert.isTrue(this.actorService.getActorLogged()
                .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        debuk.setCompany(company);

        this.assertDeCompany(debuk, company);

        if(debuk.getId()==0)
            debuk.setTicker(this.generadorDeTickers());

        Assert.isTrue(!debuk.getIsFinal());
        debuk = this.debukRepository.save(debuk);

        return debuk;
    }

    public Debuk saveAsFinal(Debuk debuk) {
        Assert.notNull(debuk);
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        this.assertDeCompany(debuk, company);

        if(debuk.getId()==0)
            debuk.setTicker(this.generadorDeTickers());

        debuk.setMoment(new Date());
        debuk.setIsFinal(true);
        debuk = this.debukRepository.save(debuk);

        return debuk;
    }

    //Reconstruct
    public Debuk reconstruct(final Debuk debuk, final BindingResult binding) {
        Debuk result;

        if (debuk.getId() != 0){
            result = this.debukRepository.findOne(debuk.getId());
            debuk.setVersion(result.getVersion());
            debuk.setTicker(result.getTicker());
        }

        result = debuk;
        this.validator.validate(debuk, binding);

        return result;
    }

    //Another methods

    public void delete(final Debuk debuk) {
        Assert.notNull(debuk);
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
        this.assertDeCompany(debuk, company);
        Assert.isTrue(!debuk.getIsFinal());
        this.debukRepository.delete(debuk);
    }

    public void deleteF(final Debuk debuk) {
        this.debukRepository.delete(debuk);
    }

    public Collection<Debuk> getDraftDebuksByCompanyOf(int id, int auditId){
        return this.debukRepository.getDraftDebuksByCompanyOf(id, auditId);
    }
    public Collection<Audit> getAuditsByAuditor(int id) { return this.debukRepository.getAuditsByAuditor(id);}
    public Collection<Debuk> getFinalDebukOf(int auditId) { return this.debukRepository.getFinalDebukOf(auditId);}
    public Collection<Debuk> getDebukByAudit(int auditId) { return this.debukRepository.getDebuksByAudit(auditId);}

    public void assertDeCompany(Debuk debuk, Company company){
        if(debuk.getId()!=0) {
            Assert.isTrue(this.actorService.getActorLogged()
                    .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
        }
    }

    public static String generadorDeTickers() {
        String dateRes;
        String numericRes = "";
        final String alphanumeric = "0123456789";
        dateRes = new SimpleDateFormat("yyMM/dd").format(Calendar.getInstance().getTime());

        for (int i = 0; i < 5; i++) {
            final Random random = new Random();
            numericRes = numericRes + alphanumeric.charAt(random.nextInt(alphanumeric.length() - 1));
        }

        return dateRes + "-" + numericRes;
    }
}
