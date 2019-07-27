package com.dev4lazy.pricecollector.model.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CountryDao {
    @Insert
    void insert(Country country);

    @Update
    void update(Country country);

    @Delete
    void delete(Country country);

    @Query("DELETE FROM countries")
    void deleteAll();

    @Query("SELECT * from countries WHERE id= :id")
    List<Country>findCountryById(String id);

    //todo findCountryByName like

    @Query("SELECT * from countries ORDER BY name ASC")
    List<Country> getAllCountries();

}
