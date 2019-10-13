package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;

public class User {

    private String login;
    private OwnStore ownStore;
    private Department department;
    private Market market;
    private Sector sector;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public OwnStore getOwnStore() {
        return ownStore;
    }

    public void setOwnStore(OwnStore ownStore) {
        this.ownStore = ownStore;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
    
    public void clear() {
        login = null;
        ownStore = null;
        department = null;
        market = null;
        sector = null;
    }
}
