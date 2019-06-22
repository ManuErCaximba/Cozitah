package services;

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
    private CompanyService companyService;

    //Validator
    @Autowired
    private Validator validator;


    //Simple CRUD Methods
    public Cozitah create() {
        Cozitah result;
        result = new Cozitah();
        result.setMoment(new Date());
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
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        this.assertDeCompany(cozitah, company);

        Assert.isTrue(!cozitah.getIsFinal());
        cozitah.setCompany(company);
        cozitah.setIsFinal(true);
        cozitah = this.cozitahRepository.save(cozitah);

        return cozitah;
    }

    public Cozitah saveAsFinal(Cozitah cozitah) {
        Assert.notNull(cozitah);
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        this.assertDeCompany(cozitah, company);

        cozitah.setCompany(company);
        cozitah = this.cozitahRepository.save(cozitah);

        return cozitah;
    }

    //Reconstruct
    public Cozitah reconstruct(final Cozitah cozitah, final BindingResult binding) {
        Cozitah result;

        result = this.cozitahRepository.findOne(cozitah.getId());

        cozitah.setVersion(result.getVersion());

        result = cozitah;
        this.validator.validate(cozitah, binding);

        return result;
    }

    //Another methods

    public void delete(final Cozitah cozitah) {
        Assert.notNull(cozitah);
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
        this.assertDeCompany(cozitah, company);
        Assert.isTrue(!cozitah.getIsFinal());
        this.cozitahRepository.delete(cozitah);
    }

    public Collection<Cozitah> getCozitahsByCompany(int id){
        return this.cozitahRepository.getCozitahsByCompany(id);
    }

    public void assertDeCompany(Cozitah cozitah, Company company){
        if(cozitah.getId()!=0) {
            Assert.isTrue(this.actorService.getActorLogged()
                    .getUserAccount().getAuthorities().iterator().next().getAuthority().equals("COMPANY"));
            Assert.isTrue(company.equals(cozitah.getCompany()));
        }
    }
}
