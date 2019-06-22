package domain;

import datatype.Url;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class Cozitah extends DomainEntity{

    private String ticker;
    private Date moment;
    private String body;
    private boolean isFinal;
    private Url picture;

    //Relations
    private Audit audit;
    private Company company;

    @NotBlank
    public boolean getIsFinal() {
        return this.isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    //@Pattern(regexp = "//d-//n")
    public String getTicker() {
        return this.ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    @NotNull
    public Date getMoment() {
        return moment;
    }

    public void setMoment(Date moment) {
        this.moment = moment;
    }

    @NotBlank
    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Url getPicture() {
        return this.picture;
    }

    public void setPicture(Url picture) {
        this.picture = picture;
    }

    //Relationships

    @Valid
    @OneToOne(optional = false)
    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    @Valid
    @ManyToOne(optional = false)
    public Company getCompany() {
        return this.company;
    }

    public void setCompany(Company company){
        this.company = company;
    }
}
