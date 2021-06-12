package com.dev4lazy.pricecollector.remote_model.enities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users"
)
public class RemoteUser {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String login; // prywatny adres e-mail
    private String name; // imię i nazwisko
    private String email; // służbowy adres e-mail
    private String ownStoreNumber; // OwnStore.ownNumber
    private String departmentSymbol; // Department.symbol
    private String sectorName; // Sector.name
    private String marketName; // Market.name

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOwnStoreNumber() {
        return ownStoreNumber;
    }

    public void setOwnStoreNumber(String ownStoreNumber) {
        this.ownStoreNumber = ownStoreNumber;
    }

    public String getDepartmentSymbol() {
        return departmentSymbol;
    }

    public void setDepartmentSymbol(String departmentSymbol) {
        this.departmentSymbol = departmentSymbol;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemoteUser)) return false;
        RemoteUser that = (RemoteUser) o;
        return id == that.id;
    }
}


