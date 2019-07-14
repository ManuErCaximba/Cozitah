package services;

import domain.Company;
import domain.Debuk;
import domain.Position;
import domain.Problem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.DataBinder;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ManageDebukTest extends AbstractTest {

    @Autowired
    private DebukService debukService;

    /*
     * Testing functional requirement :An actor that is authenticated as a auditor must be able to manage their XXXX
     * Positive: A auditor create a XXXX as draft
     * Negative: A company tries to create a XXXX
     */

    @Test
    public void createXXXXAsDraftDriver() {
        final Object testingData[][] = {
                {
                        "company1", "prueba1", "prueba1", null
                }, {
                "company2", "prueba1", "prueba1", null
        }, {
                "auditor1", "prueba1", "prueba1", IllegalArgumentException.class
        }};
        for (int i = 0; i < testingData.length; i++)
            this.createXXXXDraftTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void createXXXXDraftTemplate(final String actor, final String body, final String ticker,
                                        final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(actor);

            Debuk c = this.debukService.create();
            c.setBody(body);
            c.setTicker(ticker);
            this.debukService.saveAsDraft(c);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Positive: A auditor create a XXXX as final
     * Negative: A company tries to create a XXXX
     */

    @Test
    public void createXXXXAsFinalDriver() {
        final Object testingData[][] = {
                {
                        "company1", "prueba1", "prueba1", null
                }, {
                "company2", "prueba1", "prueba1", null
        }, {
                "auditor1", "prueba1", "prueba1", IllegalArgumentException.class
        }};
        for (int i = 0; i < testingData.length; i++)
            this.createXXXXAsFinalTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void createXXXXAsFinalTemplate(final String actor, final String body, final String ticker,
                                         final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(actor);

            Debuk c = this.debukService.create();
            c.setBody(body);
            c.setTicker(ticker);
            this.debukService.saveAsFinal(c);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Positive: A auditor edit a XXXX and save it as draft
     * Negative: A company tries to edit a XXXX
     */

    @Test
    public void editXXXXAsDraftDriver() {
        final Object testingData[][] = {
                {
                        "debuk5", "company2", "prueba", null
                }, {
                "debuk5", "auditor1", "prueba", IllegalArgumentException.class
        }};
        for (int i = 0; i < testingData.length; i++)
            this.editXXXXAsDraftTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void editXXXXAsDraftTemplate(final String debuk, final String actor, final String body,
                                         final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(actor);

            Debuk c = this.debukService.findOne(super.getEntityId(debuk));
            c.setBody(body);
            this.debukService.saveAsDraft(c);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Positive: A auditor edit a XXXX and save it as draft
     * Negative: A company tries to edit a XXXX
     */

    @Test
    public void editXXXXAsFinalDriver() {
        final Object testingData[][] = {
                {
                        "debuk5", "company2", "prueba", null
                }, {
                "debuk5", "auditor2", "prueba", IllegalArgumentException.class
        }};
        for (int i = 0; i < testingData.length; i++)
            this.editXXXXAsFinalTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (String) testingData[i][2], (Class<?>) testingData[i][3]);
    }

    private void editXXXXAsFinalTemplate(final String debuk, final String actor, final String body,
                                         final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(actor);

            Debuk c = this.debukService.findOne(super.getEntityId(debuk));
            c.setBody(body);
            this.debukService.saveAsFinal(c);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }

    /*
     * Positive: A auditor delete a XXXX in draft mode
     * Negative: A auditor tries to delete a XXXX in final mode
     */

    @Test
    public void deleteXXXXDriver() {
        final Object testingData[][] = {
                {
                        "debuk5", "company2", null
                }, {
                "debuk4TEST", "company2", IllegalArgumentException.class
        }};
        for (int i = 0; i < testingData.length; i++)
            this.deleteXXXXTemplate((String) testingData[i][0], (String) testingData[i][1],
                    (Class<?>) testingData[i][2]);
    }

    private void deleteXXXXTemplate(final String debuk, final String actor, final Class<?> expected) {
        Class<?> caught;
        caught = null;

        try {
            this.authenticate(actor);

            Debuk c = this.debukService.findOne(super.getEntityId(debuk));
            this.debukService.delete(c);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        super.checkExceptions(expected, caught);
    }
}
