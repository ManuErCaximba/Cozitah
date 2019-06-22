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
import services.ActorService;
import services.CompanyService;
import services.CozitahService;

import java.util.Collection;

@Controller
@RequestMapping("company/cozitah")
public class CozitahController extends AbstractController {

    @Autowired
    private CozitahService cozitahService;
    @Autowired
    private ActorService actorService;
    @Autowired
    private CompanyService companyService;

    //LIST
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list() {
        ModelAndView result;
        Collection<Cozitah> cozitahs;
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());
        result = new ModelAndView("company/cozitah/list");

        //Sacar cozitahs que sean de la company
        cozitahs = this.cozitahService.getCozitahsByCompany(company.getId());

        //Lenguaje
        String lang = LocaleContextHolder.getLocale().getLanguage();

        //Meter en ModelAndView
        result.addObject("cozitahs", cozitahs);
        result.addObject("company", company);
        result.addObject("lang", lang);
        result.addObject("requestURI", "company/cozitahs/list.do");

        return result;
    }
    //EDIT
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam int id) {
        ModelAndView result;
        Cozitah cozitah;

        cozitah = this.cozitahService.findOne(id);

        Assert.notNull(cozitah);
        result = this.createEditModelAndView(cozitah);

        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "draft")
    public ModelAndView saveAsDraft(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        try {
            Assert.isTrue(company.equals(cozitah.getCompany()));
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.createEditModelAndView(cozitah);
            else {
                this.cozitahService.saveAsDraft(cozitah);
                result = new ModelAndView("redirect:/company/cozitah/list.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, params = "final")
    public ModelAndView saveAsFinal(@ModelAttribute("cozitah") Cozitah cozitah, final BindingResult binding) {
        ModelAndView result;
        Company company = this.companyService.findOne(this.actorService.getActorLogged().getId());

        try {
            Assert.isTrue(company.equals(cozitah.getCompany()));
            cozitah = this.cozitahService.reconstruct(cozitah, binding);
            if (binding.hasErrors())
                result = this.createEditModelAndView(cozitah);
            else {
                this.cozitahService.saveAsFinal(cozitah);
                result = new ModelAndView("redirect:/company/cozitah/list.do");
            }
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //DELETE
    @RequestMapping(value = "/delete", method = RequestMethod.POST, params = "delete")
    public ModelAndView delete(@RequestParam int id) {
        ModelAndView result;
        Cozitah cozitah = this.cozitahService.findOne(id);
        try {
            this.cozitahService.delete(cozitah);
            result = new ModelAndView("redirect:/company/cozitah/list.do");
        } catch (final Throwable oops) {
            result = new ModelAndView("redirect:/");
        }
        return result;
    }

    //MODEL AND VIEWS
    protected ModelAndView createEditModelAndView(final Cozitah cozitah) {
        ModelAndView result;

        result = this.createEditModelAndView(cozitah, null);

        return result;
    }

    protected ModelAndView createEditModelAndView(final Cozitah cozitah, final String messageCode) {
        ModelAndView result;

        result = new ModelAndView("company/cozitah/edit");
        result.addObject("cozitah", cozitah);
        result.addObject("messageCode", messageCode);

        return result;
    }
}

