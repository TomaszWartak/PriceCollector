package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dev4lazy.pricecollector.model.entities.Country;

import java.util.List;

@Dao
public interface CountryDao {
    @Insert
    void insert(Country country);

    @Update
    void update(Country country);

    /* Żadnego usuwania danych... */
    @Delete
    void delete(Country country);

    @Query("DELETE FROM countries")
    void deleteAll();
    /**/

    @Query("SELECT * from countries ORDER BY name ASC")
    LiveData<List<Country>> getAllCountries();

    @Query("SELECT * from countries WHERE id= :id")
    LiveData<List<Country>>findCountryById(String id);

    //todo findCountryByName like


}
