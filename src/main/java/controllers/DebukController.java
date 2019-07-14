package controllers;

import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import services.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@Controller
@RequestMapping("auditor/debuk")
public class DebukController extends AbstractController {

    @Autowired
    private DebukService debukService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private AuditorService auditorService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AuditService auditService;

    //LIST
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam final int auditId) {
        ModelAndView result;
        Collection<Debuk> debuks;
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
        result = new ModelAndView("auditor/debuk/list");

        //Sacar debuks que sean de la auditor
        debuks = this.debukService.getFinalDebukOf(auditId);
        if (company != null)
            debuks.addAll(this.debukService.getDraftDebuksByCompanyOf(company.getId(), auditId));


        //Lenguaje
        String lang = LocaleContextHolder.getLocale().getLanguage();

        //Meter en ModelAndView
        result.addObject("debuks", debuks);
        result.addObject("company", company);
        result.addObject("lang", lang);
        final Date haceUnMes = this.restarMesesFecha(new Date(), 1);
        final Date haceDosMeses = this.restarMesesFecha(new Date(), 2);
        result.addObject("haceUnMes", haceUnMes);
        result.addObject("haceDosMeses", haceDosMeses);
        result.addObject("requestURI", "auditor/debuks/list.do");

        return result;
    }

    //SHOW
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int id) {
        ModelAndView result;
        Debuk debuk;
        Actor actor = this.actorService.getActorLogged();
        Company company = null;

        try {
            debuk = this.debukService.findOne(id);
            if(debuk.getCompany().equals(this.companyService.findOne(actor.getId())))
                company = this.companyService.findOne(actor.getId());
        } catch (final Exception e) {
            result = new ModelAndView("redirect:/");
            return result;
        }

        //Lenguaje
        String lang = LocaleContextHolder.getLocale().getLanguage();

        result = new ModelAndView("auditor/debuk/show");
        result.addObject("lang", lang);
        result.addObject("debuk", debuk);
        result.addObject("company", company);

        return result;
    }

    //CREATE
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int auditId) {
        ModelAndView result;
        Debuk debuk;

        debuk = this.debukService.create();

        debuk.setAudit(this.auditService.findOne(auditId));
        Assert.notNull(debuk);
        result = this.createModelAndView(debuk);

        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "draft")
    public ModelAndView createAsDraft(@ModelAttribute("debuk") Debuk debuk, final BindingResult binding) {
        ModelAndView result;

        try {
            debuk = this.debukService.reconstruct(debuk, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(debuk);
            else {
                this.debukService.saveAsDraft(debuk);
                result = new ModelAndView("redirect:/position/listNotLogged.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "final")
    public ModelAndView createAsFinal(@ModelAttribute("debuk") Debuk debuk, final BindingResult binding) {
        ModelAndView result;

        try {
            debuk = this.debukService.reconstruct(debuk, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(debuk);
            else {
                this.debukService.saveAsFinal(debuk);
                result = new ModelAndView("redirect:/position/listNotLogged.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //EDIT
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int id) {
        ModelAndView result;
        Debuk debuk;

        debuk = this.debukService.findOne(id);

        Assert.notNull(debuk);
        result = this.editModelAndView(debuk);

        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "draft")
    public ModelAndView saveAsDraft(@ModelAttribute("debuk") Debuk debuk, final BindingResult binding) {
        ModelAndView result;

        try {
            debuk = this.debukService.reconstruct(debuk, binding);
            if (binding.hasErrors())
                result = this.editModelAndView(debuk);
            else {
                this.debukService.saveAsDraft(debuk);
                result = new ModelAndView("redirect:/position/listNotLogged.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "final")
    public ModelAndView saveAsFinal(@ModelAttribute("debuk") Debuk debuk, final BindingResult binding) {
        ModelAndView result;

        try {
            debuk = this.debukService.reconstruct(debuk, binding);
            if (binding.hasErrors())
                result = this.editModelAndView(debuk);
            else {
                this.debukService.saveAsFinal(debuk);
                result = new ModelAndView("redirect:/position/listNotLogged.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //DELETE
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam int id) {
        ModelAndView result;
        Debuk debuk = this.debukService.findOne(id);
        try {
            this.debukService.delete(debuk);
            result = new ModelAndView("redirect:/position/listNotLogged.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //MODEL AND VIEWS
    protected ModelAndView createModelAndView(final Debuk debuk) {
        ModelAndView result;

        result = this.createModelAndView(debuk, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Debuk debuk, final String messageCode) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());

        result = new ModelAndView("auditor/debuk/create");
        result.addObject("debuk", debuk);
        result.addObject("messageCode", messageCode);

        return result;
    }
    protected ModelAndView editModelAndView(final Debuk debuk) {
        ModelAndView result;

        result = this.editModelAndView(debuk, null);

        return result;
    }

    protected ModelAndView editModelAndView(final Debuk debuk, final String messageCode) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());

        result = new ModelAndView("auditor/debuk/edit");
        result.addObject("debuk", debuk);
        result.addObject("messageCode", messageCode);

        return result;
    }

    private Date restarMesesFecha(final Date date, final Integer meses) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -meses);
        return calendar.getTime();
    }
}

