package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface _Dao<D> {

    Integer getNumberOf();

    LiveData<Integer> getNumberOfLiveData();

    // TODO ??? wyciąłem stąd i przeniosłem do CountryDao: @Insert(onConflict = OnConflictStrategy.IGNORE)
    // Ale wkleiłem z powrotem...
    // jeśli to działa, to przenieś również @Update i @Delete do wszystkich Dao, <-- chyba nie...
    // albo zrób interface Room_Dao, który będzie używany zamiast _Dao. TO CHYBA NAJLEPSZY POMYSŁ
    // Zrefakturyj _Dao na Room_Dao, gdzie będą tylko metody z adnotacjami Room, a wszystkie będą w _dao
    // Zobacz też OneNote Studia/ .. / Kodowanie/!! Współpraca z biblioteką Room/Stworzenie DAO/!! Przykład  uniwersalnego abstrakcyjnego DAO

    // @Insert(onConflict = OnConflictStrategy.IGNORE) - domyślnie jest ABORT
    @Insert( onConflict = OnConflictStrategy.IGNORE )
    Long insert( D data );

    // todo Czy dać tutaj metodę insertAll, updateAll?
    @Update
    int update( D data );

    @Delete
    int delete( D data );

    int deleteAll();

    List<D> getAll();

    LiveData<List<D>> getAllLiveData();

    DataSource.Factory<Integer, D> getAllPaged();

    // todo @RawQuery
    List<D> getViaQuery( SimpleSQLiteQuery query );

    // todo @RawQuery
    LiveData<List<D>> getViaQueryLiveData( SimpleSQLiteQuery query );

    List<D> findById(int id);

    LiveData<List<D>> findByIdLiveData( int id );

    List<D> findByName( String name );

}
