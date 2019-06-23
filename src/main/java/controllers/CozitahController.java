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
@RequestMapping("auditor/cozitah")
public class CozitahController extends AbstractController {

    @Autowired
    private CozitahService cozitahService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private AuditorService auditorService;
    @Autowired
    private AuditService auditService;

    //LIST
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam final int auditId) {
        ModelAndView result;
        Collection<Cozitah> cozitahs;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());
        result = new ModelAndView("auditor/cozitah/list");

        //Sacar cozitahs que sean de la auditor
        cozitahs = this.cozitahService.getFinalCozitahOf(auditId);
        if (auditor != null)
            cozitahs.addAll(this.cozitahService.getDraftCozitahsByAuditorOf(auditor.getId(), auditId));


        //Lenguaje
        String lang = LocaleContextHolder.getLocale().getLanguage();

        //Meter en ModelAndView
        result.addObject("cozitahs", cozitahs);
        result.addObject("auditor", auditor);
        result.addObject("lang", lang);
        final Date haceUnMes = this.restarMesesFecha(new Date(), 1);
        final Date haceDosMeses = this.restarMesesFecha(new Date(), 2);
        result.addObject("haceUnMes", haceUnMes);
        result.addObject("haceDosMeses", haceDosMeses);
        result.addObject("requestURI", "auditor/cozitahs/list.do?auditId="+auditId);

        return result;
    }

    //SHOW
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public ModelAndView display(@RequestParam final int id) {
        ModelAndView result;
        Cozitah cozitah;
        Actor actor = this.actorService.getActorLogged();
        Auditor auditor = null;

        try {
            cozitah = this.cozitahService.findOne(id);
            if(cozitah.getAudit().getAuditor().equals(this.auditorService.findOne(actor.getId())))
                auditor = this.auditorService.findOne(actor.getId());
        } catch (final Exception e) {
            result = new ModelAndView("redirect:/");
            return result;
        }

        //Lenguaje
        String lang = LocaleContextHolder.getLocale().getLanguage();

        result = new ModelAndView("auditor/cozitah/show");
        result.addObject("lang", lang);
        result.addObject("cozitah", cozitah);
        result.addObject("auditor", auditor);

        return result;
    }

    //CREATE
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@RequestParam int auditId) {
        ModelAndView result;
        Cozitah cozitah;

        cozitah = this.cozitahService.create();

        cozitah.setAudit(this.auditService.findOne(auditId));
        Assert.notNull(cozitah);
        result = this.createModelAndView(cozitah);

        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "draft")
    public ModelAndView createAsDraft(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;

        try {
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(cozitah);
            else {
                this.cozitahService.saveAsDraft(cozitah);
                result = new ModelAndView("redirect:/audit/auditor/list.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, params = "final")
    public ModelAndView createAsFinal(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());

        try {
            Assert.isTrue(auditor.equals(cozitah.getAudit().getAuditor()));
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.createModelAndView(cozitah);
            else {
                this.cozitahService.saveAsFinal(cozitah);
                result = new ModelAndView("redirect:/audit/auditor/list.do");
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
        Cozitah cozitah;

        cozitah = this.cozitahService.findOne(id);

        Assert.notNull(cozitah);
        result = this.editModelAndView(cozitah);

        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "draft")
    public ModelAndView saveAsDraft(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());

        try {
            Assert.isTrue(auditor.equals(cozitah.getAudit().getAuditor()));
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.editModelAndView(cozitah);
            else {
                this.cozitahService.saveAsDraft(cozitah);
                result = new ModelAndView("redirect:/audit/auditor/list.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "final")
    public ModelAndView saveAsFinal(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());

        try {
            Assert.isTrue(auditor.equals(cozitah.getAudit().getAuditor()));
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.editModelAndView(cozitah);
            else {
                this.cozitahService.saveAsFinal(cozitah);
                result = new ModelAndView("redirect:/audit/auditor/list.do");
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
        Cozitah cozitah = this.cozitahService.findOne(id);
        int auditId = cozitah.getAudit().getId();
        try {
            this.cozitahService.delete(cozitah);
            result = new ModelAndView("redirect:/audit/auditor/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //MODEL AND VIEWS
    protected ModelAndView createModelAndView(final Cozitah cozitah) {
        ModelAndView result;

        result = this.createModelAndView(cozitah, null);

        return result;
    }

    protected ModelAndView createModelAndView(final Cozitah cozitah, final String messageCode) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());
        Collection<Audit> audits = this.cozitahService.getAuditsByAuditor(auditor.getId());

        result = new ModelAndView("auditor/cozitah/create");
        result.addObject("cozitah", cozitah);
        result.addObject("audits", audits);
        result.addObject("messageCode", messageCode);

        return result;
    }
    protected ModelAndView editModelAndView(final Cozitah cozitah) {
        ModelAndView result;

        result = this.editModelAndView(cozitah, null);

        return result;
    }

    protected ModelAndView editModelAndView(final Cozitah cozitah, final String messageCode) {
        ModelAndView result;
        Auditor auditor = this.auditorService.findOne(this.actorService.getActorLogged().getId());
        Collection<Audit> audits = this.cozitahService.getAuditsByAuditor(auditor.getId());

        result = new ModelAndView("auditor/cozitah/edit");
        result.addObject("cozitah", cozitah);
        result.addObject("audits", audits);
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

