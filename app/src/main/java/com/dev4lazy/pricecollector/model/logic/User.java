package com.dev4lazy.pricecollector.model.logic;

import com.dev4lazy.pricecollector.model.entities.Department;
import com.dev4lazy.pricecollector.model.entities.Market;
import com.dev4lazy.pricecollector.model.entities.OwnStore;
import com.dev4lazy.pricecollector.model.entities.Sector;
import com.dev4lazy.pricecollector.remote_model.enities.RemoteUser;

public class User {

    private String login;
    private String name;
    private String email; // nie używane
    private OwnStore ownStore;
    private Department department;
    private Market market; // nie używane
    private Sector sector;

    public User() {
        login = "";
    }

    public User(RemoteUser remoteUser) {
        login = remoteUser.getLogin();
        name = remoteUser.getName();
        email = remoteUser.getEmail();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
