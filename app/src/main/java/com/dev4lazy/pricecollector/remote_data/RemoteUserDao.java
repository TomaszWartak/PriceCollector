package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.dev4lazy.pricecollector.model.db._Dao;

import java.util.List;

@Dao
public interface RemoteUserDao extends _Dao<RemoteUser> {

    @Override
    @Query("SELECT COUNT(*) FROM users")
    Integer getNumberOf();

    @Override
    @Query("DELETE FROM users")
    int deleteAll();

    @Override
    @Query("SELECT * from users ORDER BY name ASC")
    List<RemoteUser> getAll();

    @Override
    @Query("SELECT * from users ORDER BY name ASC")
    LiveData<List<RemoteUser>> getAllLiveData();

    @Override
    @Query("SELECT * from users ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteUser> getAllPaged();

    @Override
    @RawQuery(observedEntities = RemoteUser.class)
    List<RemoteUser> getViaQuery(SimpleSQLiteQuery query);

    @RawQuery(observedEntities = RemoteUser.class)
    LiveData<List<RemoteUser>> getViaQueryLiveData(SimpleSQLiteQuery query);

    @Override
    @Query("SELECT * from users WHERE id= :id")
    List<RemoteUser> findById(int id);

    @Override
    @Query("SELECT * from users WHERE id= :id")
    LiveData<List<RemoteUser>> findByIdLiveData( int id );

    @Override
    @Query("SELECT * from users WHERE name= :name")
    List<RemoteUser> findByName(String name);

    /* todo
    @Query("SELECT * from users WHERE login= :login")
    List<RemoteUser> findByLogin(String login);

     */

}
