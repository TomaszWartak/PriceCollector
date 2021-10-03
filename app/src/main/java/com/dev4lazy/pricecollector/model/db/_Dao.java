package com.dev4lazy.pricecollector.model.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.List;

@Dao
public interface _Dao<D> {

    Integer getNumberOf();

    @RawQuery
    Integer getNumberOfViaQuery( SimpleSQLiteQuery query );

    LiveData<Integer> getNumberOfLiveData();

    // TODO XXXXXX ??? wyciąłem stąd i przeniosłem do CountryDao: @Insert(onConflict = OnConflictStrategy.IGNORE)
    // Ale wkleiłem z powrotem...
    // jeśli to działa, to przenieś również @Update i @Delete do wszystkich Dao, <-- chyba nie...
    // albo zrób interface Room_Dao, który będzie używany zamiast _Dao. TO CHYBA NAJLEPSZY POMYSŁ
    // Zrefaktoryzuj _Dao na Room_Dao, gdzie będą tylko metody z adnotacjami Room, a wszystkie będą w _dao
    // Zobacz też OneNote Studia/ .. / Kodowanie/!! Współpraca z biblioteką Room/Stworzenie DAO/!! Przykład  uniwersalnego abstrakcyjnego DAO

    @Insert( onConflict = OnConflictStrategy.IGNORE ) // <-- domyślnie jest OnConflictStrategy.ABORT
    /**
     * Zwraca id zapisanej danej
     */
    Long insert( D data );

    // todo Czy dać tutaj metodę insertAll, updateAll?
    @Update
    /**
     * Zwraca ilość zaktualizowanych wierszy
     */
    int update( D data );

    @Delete
    /**
     * Zwraca ilość usuniętych wierszy
     */
    int delete( D data );

    /*
    @Delete
    int deleteById(int id);
    */

    int deleteAll();

    List<D> getAll();

    LiveData<List<D>> getAllLiveData();

    DataSource.Factory<Integer, D> getAllPaged();

    List<D> getViaQuery( SimpleSQLiteQuery query );

    LiveData<List<D>> getViaQueryLiveData( SimpleSQLiteQuery query );

    // TODO Jeśli chcesz używać tej metody, to musisz ją zaimplementować w kazdym DAO
    // DataSource.Factory<Integer, D> getViaQueryPaged( SimpleSQLiteQuery query );

    List<D> findById(int id);

    LiveData<List<D>> findByIdLiveData( int id );

    List<D> findByName( String name );

}
