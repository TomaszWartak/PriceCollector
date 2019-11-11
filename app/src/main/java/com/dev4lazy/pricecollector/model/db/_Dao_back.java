package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface _Dao_back<D> {

    Integer getNumberOf();

    // TODO ??? wyciąłem stąd i przeniosłem do CountryDao: @Insert(onConflict = OnConflictStrategy.IGNORE)
    // jeśli to działa, to przenieś również @Update i @Delete do wszystkich Dao,
    // albo zrób interface Room_Dao, który będzie używany zamiast _Dao
    Long insert(D data);

    @Update
    int update(D data);

    @Delete
    int delete(D data);

    int deleteAll();

    List<D> getAll();

    LiveData<List<D>> getAllLiveData();

    DataSource.Factory<Integer, D> getAllPaged();

    List<D> findById(int id);

    LiveData<List<D>> findByIdLiveData(int id);

    List<D> findByName(String name);

    @RawQuery
    List<D> getViaQuery(SimpleSQLiteQuery query);

    @RawQuery
    LiveData<List<D>> getViaQueryLiveData(SimpleSQLiteQuery query);

}
