package com.dev4lazy.pricecollector.remote_data;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.dev4lazy.pricecollector.model.db.StoreDao;
import com.dev4lazy.pricecollector.model.db._Dao;
import com.dev4lazy.pricecollector.model.logic.User;

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

    @Query("SELECT * from users ORDER BY name ASC")
    DataSource.Factory<Integer, RemoteUser> getAllRemoteUsersPaged();

    @RawQuery(observedEntities = RemoteUser.class)
    LiveData<List<RemoteUser>> getViaQueryLiveData(SupportSQLiteQuery query);

    @Override
    @Query("SELECT * from users WHERE id= :id")
    List<RemoteUser> findById(int id);

    @Override
    @Query("SELECT * from users WHERE name= :name")
    List<RemoteUser> findByName(String name);

    @Query("SELECT * from users WHERE login= :login")
    List<RemoteUser> findByLogin(String login);

}
