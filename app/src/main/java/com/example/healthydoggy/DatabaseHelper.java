package com.example.healthydoggy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 数据库信息
    private static final String DATABASE_NAME = "HealthyDoggyDB";
    private static final int DATABASE_VERSION = 3;

    // 用户表
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "_id";
    public static final String COL_USER_NAME = "username";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PHONE = "phone";

    // 帖子表
    public static final String TABLE_POSTS = "posts";
    public static final String COL_POST_ID = "_id";
    public static final String COL_POST_TITLE = "title";
    public static final String COL_POST_CONTENT = "content";
    public static final String COL_POST_AUTHOR = "author";
    public static final String COL_POST_TIME = "time";

    // 消息表
    public static final String TABLE_MESSAGES = "messages";
    public static final String COL_MSG_ID = "_id";
    public static final String COL_MSG_USER = "username";
    public static final String COL_MSG_CONTENT = "content";
    public static final String COL_MSG_TIME = "time";
    public static final String COL_MSG_POST_ID = "post_id";

    // 商品表
    public static final String TABLE_PRODUCTS = "products";
    public static final String COL_PRODUCT_ID = "_id";
    public static final String COL_PRODUCT_NAME = "name";
    public static final String COL_PRODUCT_PRICE = "price";
    public static final String COL_PRODUCT_DESC = "description";

    // 健康记录表
    public static final String TABLE_HEALTH = "health_records";
    public static final String COL_HEALTH_ID = "_id";
    public static final String COL_HEALTH_DATE = "date";
    public static final String COL_HEALTH_WEIGHT = "weight";
    public static final String COL_HEALTH_TEMP = "temperature";

    // 创建表语句
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
            COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_NAME + " TEXT UNIQUE NOT NULL, " +
            COL_USER_PASSWORD + " TEXT NOT NULL, " +
            COL_USER_EMAIL + " TEXT, " +
            COL_USER_PHONE + " TEXT)";

    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + " (" +
            COL_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_POST_TITLE + " TEXT NOT NULL, " +
            COL_POST_CONTENT + " TEXT NOT NULL, " +
            COL_POST_AUTHOR + " TEXT NOT NULL, " +
            COL_POST_TIME + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE " + TABLE_MESSAGES + " (" +
            COL_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_MSG_USER + " TEXT NOT NULL, " +
            COL_MSG_CONTENT + " TEXT NOT NULL, " +
            COL_MSG_TIME + " TEXT NOT NULL, " +
            COL_MSG_POST_ID + " INTEGER NOT NULL)";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_PRODUCT_NAME + " TEXT NOT NULL, " +
            COL_PRODUCT_PRICE + " REAL NOT NULL, " +
            COL_PRODUCT_DESC + " TEXT NOT NULL)";

    private static final String CREATE_TABLE_HEALTH = "CREATE TABLE " + TABLE_HEALTH + " (" +
            COL_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_HEALTH_DATE + " TEXT NOT NULL, " +
            COL_HEALTH_WEIGHT + " REAL NOT NULL, " +
            COL_HEALTH_TEMP + " REAL NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_HEALTH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        onCreate(db);
    }

    // 新增：添加全局交流消息（用-1标识为非帖子评论的全局消息）
    public long addGlobalMessage(String username, String content, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MSG_USER, username);
        values.put(COL_MSG_CONTENT, content);
        values.put(COL_MSG_TIME, time);
        values.put(COL_MSG_POST_ID, -1); // 用-1标识全局消息
        long id = db.insert(TABLE_MESSAGES, null, values);
        db.close();
        return id;
    }

    // 新增：获取所有全局交流消息
    public Cursor getAllGlobalMessages() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_MESSAGES, null,
                COL_MSG_POST_ID + "=?",
                new String[]{"-1"}, // 查询标识为全局消息的记录
                null, null, COL_MSG_TIME + " ASC");
    }

    // 帖子相关方法
    public long addPost(String title, String content, String author, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_POST_TITLE, title);
        values.put(COL_POST_CONTENT, content);
        values.put(COL_POST_AUTHOR, author);
        values.put(COL_POST_TIME, time);
        long id = db.insert(TABLE_POSTS, null, values);
        db.close();
        return id;
    }

    // 帖子评论相关方法
    public long addMessageWithPostId(String username, String content, String time, long postId) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MSG_USER, username);
        values.put(COL_MSG_CONTENT, content);
        values.put(COL_MSG_TIME, time);
        values.put(COL_MSG_POST_ID, postId);
        long id = db.insert(TABLE_MESSAGES, null, values);
        db.close();
        return id;
    }

    // 用户注册
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, username);
        values.put(COL_USER_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    // 验证登录
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USER_NAME + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // 获取所有帖子
    public Cursor getAllPosts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_POSTS, null, null, null, null, null, COL_POST_TIME + " DESC");
    }

    // 根据帖子ID获取评论
    public Cursor getMessagesByPostId(long postId) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_MESSAGES, null,
                COL_MSG_POST_ID + "=?",
                new String[]{String.valueOf(postId)},
                null, null, COL_MSG_TIME + " ASC");
    }

    // 用户信息相关
    public Cursor getUserInfo() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_USERS, new String[]{COL_USER_NAME, COL_USER_EMAIL, COL_USER_PHONE},
                null, null, null, null, null);
    }

    public int updateUserInfo(String name, String email, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PHONE, phone);
        int rows = db.update(TABLE_USERS, values, COL_USER_NAME + "=?", new String[]{name});
        db.close();
        return rows;
    }

    public long addUserInfo(String name, String email, String phone) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PHONE, phone);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public int clearUserInfo() {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_USERS, null, null);
        db.close();
        return rows;
    }

    // 商品相关
    public long addProduct(String name, double price, String desc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_DESC, desc);
        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    public Cursor getProducts() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
    }

    // 健康记录相关方法
    public long addHealthRecord(String date, double weight, double temp) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HEALTH_DATE, date);
        values.put(COL_HEALTH_WEIGHT, weight);
        values.put(COL_HEALTH_TEMP, temp);
        long result = db.insert(TABLE_HEALTH, null, values);
        db.close();
        return result;
    }

    public Cursor getHealthRecords() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_HEALTH, null, null, null, null, null, COL_HEALTH_DATE + " DESC");
    }

    public int deleteHealthRecord(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE_HEALTH, COL_HEALTH_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rows;
    }
}