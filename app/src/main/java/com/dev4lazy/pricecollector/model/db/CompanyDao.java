package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.dev4lazy.pricecollector.model.entities.Company;

import java.util.List;


@Dao
public interface CompanyDao {

    @Insert
    void insert(Company user);

    @Update
    void update(Company user);

    @Delete
    void delete(Company user);

    @Query("DELETE FROM companies")
    void deleteAll();

    @Query("SELECT * from companies ORDER BY name ASC")
    LiveData<List<Company>> getAllCompanies();

    @Query("SELECT * from companies WHERE id= :id")
    LiveData<List<Company>> findCompanyById(String id);

    //todo findCompanyByName like

}
