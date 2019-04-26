package com.smartlock.dao;

import com.smartlock.app.SmartLockApp;
import com.smartlock.dao.gen.DaoSession;
import com.smartlock.dao.gen.KeyDao;
import com.smartlock.model.Key;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;


public class DbService {
    private static boolean DBG = true;
    private static DaoSession daoSession = SmartLockApp.getDaoSession();
    private static KeyDao keyDao = daoSession.getKeyDao();
    private static QueryBuilder queryBuilder = keyDao.queryBuilder();

//    /**
//     * get keys by accessToken
//     * @param accessToken
//     * @return
//     */
//    public static List<Key> getKeysByAccessToken(String accessToken) {
//        queryBuilder = keyDao.queryBuilder();
//       return queryBuilder.where(KeyDao.Properties.AccessToken.eq(accessToken)).list();
//    }

    /**
     * get key by accessToken and lockmac
     *
     * @param lockmac
     * @return
     */
    public static Key getKeyByLockmac(String lockmac) {
        queryBuilder = keyDao.queryBuilder();
        List<Key> keys = queryBuilder.where(KeyDao.Properties.LockMac.eq(lockmac)).list();
        if (keys.size() > 0) {
//            LogUtil.d("keys.size():" + keys.size(), DBG);
//            LogUtil.d("key:" + keys.get(0).toString(), DBG);
            return keys.get(0);
        } else return null;
    }

    /**
     * save key
     *
     * @param key
     */
    public static void saveKey(Key key) {
        keyDao.save(key);
    }

    /**
     * delete key
     *
     * @param key
     */
    public static void deleteKey(Key key) {
        keyDao.delete(key);
    }

    /**
     * save key list
     */
    public static void saveKeyList(List<Key> keys) {
        keyDao.saveInTx(keys);
    }

    /**
     * clear all keys
     */
    public static void deleteAllKey() {
        keyDao.deleteAll();
    }

    /**
     * update key
     *
     * @param key
     */
    public static void updateKey(Key key) {
        keyDao.update(key);
    }
}
