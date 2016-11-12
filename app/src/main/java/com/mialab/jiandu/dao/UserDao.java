package com.mialab.jiandu.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.mialab.jiandu.entity.User;
import com.mialab.jiandu.utils.DBHelper;

import java.sql.SQLException;

/**
 * Created by Wesly186 on 2016/8/20.
 */
public class UserDao {

    private Context context;
    private Dao<User, String> userDaoOpe;
    private DBHelper helper;

    public UserDao(Context context) {
        this.context = context;
        try {
            helper = DBHelper.getHelper(context);
            userDaoOpe = helper.getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param user
     */
    public void add(User user) {
        try {
            userDaoOpe.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        try {
            userDaoOpe.update(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getByPhone(String id) {
        User user = null;
        try {
            user = userDaoOpe.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public void deleteById(String id) {
        try {
            userDaoOpe.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
