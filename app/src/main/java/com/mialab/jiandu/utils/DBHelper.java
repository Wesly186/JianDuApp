package com.mialab.jiandu.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.mialab.jiandu.app.JianDuApplication;
import com.mialab.jiandu.conf.GlobalConf;
import com.mialab.jiandu.entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wesly186 on 2016/8/17.
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "jiandu.db";
    private static DBHelper instance;
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private DBHelper(Context context) {
        super(context, TABLE_NAME, null, 2);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DBHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context);
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        PrefUtils.setString(JianDuApplication.getContext(), GlobalConf.PHONE, "");
        PrefUtils.setString(JianDuApplication.getContext(), GlobalConf.ACCESS_TOKEN, "");
        PrefUtils.setString(JianDuApplication.getContext(), GlobalConf.REFRESH_TOKEN, "");
        try {
            TableUtils.dropTable(connectionSource, User.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        } else {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
