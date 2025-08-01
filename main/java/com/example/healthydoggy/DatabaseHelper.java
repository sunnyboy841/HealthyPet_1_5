// main/java/com/example/healthydoggy/DatabaseHelper.java
package com.example.healthydoggy;
//包的声明，与其文件夹名一致，路径D:\AndroidCODE\HealthyDoggy\app\src\main\java\com\example\healthydoggy

import android.content.ContentValues;
/*
ContentValues 是 Android 提供的一个数据容器类，用于存储键值对（类似 Map）。
主要用于 SQLite 数据库的插入（insert） 和更新（update） 操作，将需要操作的数据以键值对形式封装，方便传递给数据库 API。
例如：values.put("name", "旺财") 表示存储一个键为 "name"、值为 "旺财" 的数据。
*/
import android.content.Context;
/*Context 是 Android 应用的上下文对象，代表应用的运行环境，提供访问系统资源、启动组件、获取系统服务等功能。
操作数据库时，SQLiteOpenHelper 的初始化需要传入Context，以便数据库能关联到当前应用的上下文环境（如获取应用私有存储路径）。*/
import android.database.sqlite.SQLiteDatabase;
/*SQLiteDatabase 是 Android 中操作 SQLite 数据库的核心类，提供了直接执行 SQL 语句或调用封装方法（如insert、query、update、delete）的能力。*/
import android.database.sqlite.SQLiteOpenHelper;
/*SQLiteOpenHelper 是一个辅助类，用于管理 SQLite 数据库的创建、版本升级和打开操作，简化数据库的生命周期管理。
通常需要自定义类继承它，并实现onCreate()（数据库首次创建时调用，用于建表）和onUpgrade()（数据库版本更新时调用，用于更新表结构）方法。*/
import android.database.Cursor;
/*Cursor 是数据库查询结果的游标类，用于遍历和访问查询返回的数据集。
例如执行query方法后会返回Cursor对象，通过它的moveToFirst()、getString()、getInt()等方法可以逐行读取查询结果中的数据。
使用后需调用close()方法关闭，避免内存泄漏。
*/

/*
* 总结：
这些类共同构成了 Android 操作 SQLite 数据库的基础框架：SQLiteOpenHelper 管理数据库生命周期，
SQLiteDatabase 执行具体操作，ContentValues 封装数据，Cursor 处理查询结果，而Context 提供运行环境支持。
 */



/**
 * 数据库帮助类，用于管理应用中的SQLite数据库
 * 负责创建数据库、表结构，以及提供数据的增删改查方法
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //extends:继承
    // 数据库名称：应用的SQLite数据库文件名为"HealthyDoggyDB"
    private static final String DATABASE_NAME = "HealthyDoggyDB";
    // 数据库版本号：当前版本为2，用于数据库升级管理（static final：静态常量，后面的变量应当用全大写，业界习惯）
    //双引号引起来的，是这个变量的“值”，这里是string型
    private static final int DATABASE_VERSION = 2; // 升级版本号，触发数据库更新

    // 健康数据表：存储狗狗的健康记录（体重、体温等）
    public static final String TABLE_HEALTH = "health_records";
    // 健康表主键：自增ID，唯一标识每条健康记录
    public static final String COL_HEALTH_ID = "_id";
    // 健康记录日期：存储记录的日期，格式通常为"yyyy-MM-dd"
    //其中COL 是 列（column）的缩写，表示“这是一个列名”，与表名（TABLE）易于区分
    public static final String COL_HEALTH_DATE = "date";
    // 狗狗体重：存储体重数值，类型为浮点型（如3.2kg）
    public static final String COL_HEALTH_WEIGHT = "weight";
    // 狗狗体温：存储体温数值，类型为浮点型（如38.5℃）
    public static final String COL_HEALTH_TEMP = "temperature";

    // 商品数据表：存储应用中展示的商品信息
    public static final String TABLE_PRODUCTS = "products";
    // 商品表主键：自增ID，唯一标识每个商品
    public static final String COL_PRODUCT_ID = "_id";
    // 商品名称：存储商品的名称（如"天然狗粮 1.5kg"）
    public static final String COL_PRODUCT_NAME = "name";
    // 商品价格：存储商品价格，类型为浮点型（如59.9元）
    public static final String COL_PRODUCT_PRICE = "price";
    // 商品描述：存储商品的详细说明文字
    public static final String COL_PRODUCT_DESC = "description";

    // 论坛帖子表：存储用户发布的论坛帖子
    public static final String TABLE_POSTS = "forum_posts";
    // 帖子表主键：自增ID，唯一标识每个帖子
    public static final String COL_POST_ID = "_id";
    // 帖子标题：存储帖子的标题文字
    public static final String COL_POST_TITLE = "title";
    // 帖子作者：存储发布帖子的用户名
    public static final String COL_POST_AUTHOR = "author";
    // 帖子内容：存储帖子的详细内容
    public static final String COL_POST_CONTENT = "content";
    // 帖子浏览量：存储帖子被查看的次数，类型为整数
    public static final String COL_POST_VIEWS = "views";

    // 聊天消息表：存储用户之间的聊天记录
    public static final String TABLE_MESSAGES = "messages";
    // 消息表主键：自增ID，唯一标识每条消息
    public static final String COL_MSG_ID = "_id";
    // 消息发送者：存储发送消息的用户名
    public static final String COL_MSG_USER = "username";
    // 消息内容：存储消息的具体文字内容
    public static final String COL_MSG_CONTENT = "content";
    // 消息时间：存储消息发送的时间，格式通常为"yyyy-MM-dd HH:mm:ss"
    public static final String COL_MSG_TIME = "time";

    // 用户信息表：存储用户的个人资料信息
    public static final String TABLE_USER = "user_info";
    // 用户信息表主键：自增ID，唯一标识每条用户信息
    public static final String COL_USER_ID = "_id";
    // 用户姓名：存储用户的真实姓名或昵称
    public static final String COL_USER_NAME = "name";
    // 用户邮箱：存储用户的电子邮箱地址
    public static final String COL_USER_EMAIL = "email";
    // 用户电话：存储用户的手机号码
    public static final String COL_USER_PHONE = "phone";

    // 新增：用户账号表：存储用户登录的账号和密码
    public static final String TABLE_ACCOUNTS = "user_accounts";
    // 账号表主键：自增ID，唯一标识每个账号
    public static final String COL_ACCOUNT_ID = "_id";
    // 登录用户名：用于登录的账号，设置为唯一（UNIQUE）避免重复注册
    public static final String COL_ACCOUNT_USERNAME = "username";
    // 登录密码：存储用户设置的登录密码（实际开发中建议加密存储）
    public static final String COL_ACCOUNT_PASSWORD = "password";

    // 创建健康记录表的SQL语句
    private static final String CREATE_TABLE_HEALTH = "CREATE TABLE " + TABLE_HEALTH + " (" +
            COL_HEALTH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            COL_HEALTH_DATE + " TEXT, " + // 日期文本
            COL_HEALTH_WEIGHT + " REAL, " + // 浮点型体重
            COL_HEALTH_TEMP + " REAL)"; // 浮点型体温

    /*
数据库基本信息常量:
DATABASE_NAME: 数据库文件的名称（HealthyDoggyDB）。
DATABASE_VERSION: 数据库的版本号（2）。用于管理数据库的升级和降级。
表名常量 (TABLE_XXX):
TABLE_HEALTH, TABLE_PRODUCTS, TABLE_POSTS, TABLE_MESSAGES, TABLE_USER, TABLE_ACCOUNTS。
这些常量存储了数据库中各个表 (Table) 的实际名称（如 "health_records", "products" 等）。它们代表了数据的不同类别。
列名常量 (COL_XXX_XXX):
COL_HEALTH_ID, COL_HEALTH_DATE, COL_PRODUCT_NAME, COL_POST_TITLE, COL_MSG_USER, COL_USER_EMAIL, COL_ACCOUNT_USERNAME 等。
这些常量存储了每个表中各个列 (Column) 的实际名称（如 "_id", "date", "name", "title", "username" 等）。它们代表了数据的具体字段。
    */

    // 创建商品表的SQL语句
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
            //Create Table：关键字“创建表”
            //TABLE_PRODUCTS 表名
            //CREATE_TABLE_PRODUCTS  方法名（本质仍是文本字符串，所以类型是String）
            //每个字段以加号相连，如果是关键字（包括括号）就要用双引号引住，变量名不用
            COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            //INTEGER：整数
            //PRIMARY KEY：主键（不会重复，系统自动生成唯一id）
            //AUTOINCREMENT：自动赋值
            COL_PRODUCT_NAME + " TEXT, " + // 商品名称
            COL_PRODUCT_PRICE + " REAL, " + // 商品价格（浮点型）
            COL_PRODUCT_DESC + " TEXT)"; // 商品描述

    // 创建论坛帖子表的SQL语句
    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + " (" +
            COL_POST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            COL_POST_TITLE + " TEXT, " + // 帖子标题
            COL_POST_AUTHOR + " TEXT, " + // 作者名
            COL_POST_CONTENT + " TEXT, " + // 帖子内容
            COL_POST_VIEWS + " INTEGER)"; // 浏览量（整数）

    // 创建聊天消息表的SQL语句
    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE " + TABLE_MESSAGES + " (" +
            COL_MSG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            COL_MSG_USER + " TEXT, " + // 发送者用户名
            COL_MSG_CONTENT + " TEXT, " + // 消息内容
            COL_MSG_TIME + " TEXT)"; // 发送时间

    // 创建用户信息表的SQL语句
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            COL_USER_NAME + " TEXT, " + // 用户姓名
            COL_USER_EMAIL + " TEXT, " + // 电子邮箱
            COL_USER_PHONE + " TEXT)"; // 手机号码

    // 新增：创建用户账号表的SQL语句
    private static final String CREATE_TABLE_ACCOUNTS = "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
            COL_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // 主键自增
            COL_ACCOUNT_USERNAME + " TEXT UNIQUE, " +  // 用户名唯一（避免重复注册）
            COL_ACCOUNT_PASSWORD + " TEXT)"; // 密码（实际开发需加密）





    /*
    //context:提供应用的运行环境信息
    SQLiteOpenHelper 需要知道数据库文件应该存储在应用的哪个私有目录下。在 Android 中，每个应用都有一个私有的存储空间（通常是 /data/data/<your_package_name>/databases/）。
    // null:
    类型: SQLiteDatabase.CursorFactory (或 CursorFactory)
    Cursor 是 Android 中用于从数据库查询结果中读取数据的对象（可以想象成一个指向查询结果集某一行的指针）
    CursorFactory 允许您在 Cursor 被创建时对其进行定制，例如，可以创建一个支持特定功能的自定义 Cursor 实现。
    传入 null 是最常见和最简单的做法。这样不使用自定义的 CursorFactory，
    而是让 SQLiteOpenHelper 使用系统默认的工厂来创建标准的 Cursor 对象，一般够用
    //DATABASE_VERSION：数据库版本号
    //DATABASE_NAME:数据库名
    */
    public DatabaseHelper(Context context) {
        //函数入口在此（创建数据库）
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(...)  调用其父类，（SQLiteOpenHelper）
     }



    /**
     * 数据库首次创建时调用：执行创建表的SQL语句
     * @param db 数据库实例
     */
    @Override    //@Override：重写，说明这里重写一个，与其父类 SQLiteOpenHelper 中同名的方法。
    public void onCreate(SQLiteDatabase db) {
        // 创建所有表db.execSQL
        db.execSQL(CREATE_TABLE_HEALTH);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_POSTS);
        db.execSQL(CREATE_TABLE_MESSAGES);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ACCOUNTS); // 新增用户账号表

        // initTestData：初始化一些测试数据（方便开发调试）
        initTestData(db);
    }

    /**
     * 数据库版本升级时调用：删除旧表并重新创建（简单升级策略）
     * @param db 数据库实例
     * @param oldVersion 旧版本号
     * @param newVersion 新版本号
     */

    @Override
    //注意：虽然老版本号和新版本号在这里没有实际用到，但是一定要写，是规定
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库时删除旧表并重新创建（注意：此方式会丢失旧数据，正式环境需谨慎使用）
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HEALTH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS); // 新增用户账号表的删除
        onCreate(db);
    }


    /*
ContentValues: 这是 Android 提供的一个容器类 (Container Class)，专门用于存储键值对 (key-value pairs)。
        它的键 (key) 是字符串，代表数据库表中的列名 (Column Name)；它的值 (value) 可以是各种基本数据类型（如 String, Integer, Float, Double, Boolean 等）或 byte[]。
healthValues.put(COL_HEALTH_DATE, "2023-10-01")：向 ContentValues 中添加一个键值对
    put(): ContentValues 类的一个方法，用于添加或设置一个键值对。
    COL_HEALTH_DATE: 这是键 (Key)。它是一个字符串常量，其值是 "date"（或类似值），代表 health_records 表中的“日期”列。
    "2023-10-01": 这是值 (Value)。一个字符串，表示具体的日期值。这里使用了 YYYY-MM-DD 的标准日期格式。
    作用: 这行代码告诉 healthValues 对象：“在名为 date 的列里，要存入的值是 '2023-10-01'”。
healthValues.put(COL_HEALTH_WEIGHT, 3.2);
    添加体重数据。
    COL_HEALTH_WEIGHT: 键，对应表中的“体重”列（常量值可能是 "weight"）。
    3.2: 值，一个 double 类型的数值，表示体重为 3.2 千克。ContentValues 能自动处理这种基本数值类型。
    作用: “在名为 weight 的列里，存入 3.2”
healthValues.put(COL_HEALTH_TEMP, 38.5);
    添加体温数据。
    COL_HEALTH_TEMP: 键，对应“体温”列（常量值可能是 "temperature"）。
    38.5: 值，一个 double 类型的数值，表示体温 38.5 摄氏度。
    作用: “在名为 temperature 的列里，存入 38.5”。
db.insert(TABLE_HEALTH, null, healthValues);
    执行插入操作。
    db: 这是传入的 SQLiteDatabase 对象，代表了当前操作的数据库。
    .insert(): SQLiteDatabase 类的一个方法，专门用于向表中插入一条新记录。
    TABLE_HEALTH: 第一个参数，指定要插入数据的表名。这是一个字符串常量，值为 "health_records"。
    null: 第二个参数，nullColumnHack。
    这是一个可选的“hack”参数。它的作用是：如果 ContentValues 为空（即没有任何键值对），并且这个参数不为 null，那么 insert() 方法会强制插入一个只包含这个指定列（nullColumnHack 的值）且其值为 NULL 的行，以避免插入空行时报错。
    在绝大多数情况下，包括这里，ContentValues 都不为空，所以这个参数传 null 就可以了。 传 null 是标准做法。
    healthValues: 第三个参数，即包含了要插入数据的 ContentValues 对象。
    作用: 这行代码是执行者。它告诉数据库 db：“请把 healthValues 里包含的所有数据，作为一条新记录，插入到名为 health_records 的表中。”
    返回值: insert() 方法会返回一个 long 类型的值，表示新插入记录的行 ID (row ID)。如果插入失败，会返回 -1。虽然这里没有接收返回值，但这个机制存在。
     */
    private void initTestData(SQLiteDatabase db) {
        // 添加健康数据
        ContentValues healthValues = new ContentValues();
        healthValues.put(COL_HEALTH_DATE, "2023-10-01"); // 日期
        healthValues.put(COL_HEALTH_WEIGHT, 3.2); // 体重3.2kg
        healthValues.put(COL_HEALTH_TEMP, 38.5); // 体温38.5℃
        db.insert(TABLE_HEALTH, null, healthValues);

        // 添加商品数据
        ContentValues productValues = new ContentValues();
        productValues.put(COL_PRODUCT_NAME, "天然狗粮 1.5kg"); // 商品名称
        productValues.put(COL_PRODUCT_PRICE, 59.9); // 价格59.9元
        productValues.put(COL_PRODUCT_DESC, "天然无添加，适合全年龄段狗狗"); // 描述
        db.insert(TABLE_PRODUCTS, null, productValues);

        // 添加论坛帖子
        ContentValues postValues = new ContentValues();
        postValues.put(COL_POST_TITLE, "如何训练狗狗定点排便？"); // 标题
        postValues.put(COL_POST_AUTHOR, "铲屎官小明"); // 作者
        postValues.put(COL_POST_CONTENT, "我家狗狗总是乱排便，有什么好方法训练吗？"); // 内容
        postValues.put(COL_POST_VIEWS, 1254); // 浏览量1254次
        db.insert(TABLE_POSTS, null, postValues);

        // 添加用户信息
        ContentValues userValues = new ContentValues();
        userValues.put(COL_USER_NAME, "铲屎官小李"); // 用户名
        userValues.put(COL_USER_EMAIL, "xiaoli@example.com"); // 邮箱
        userValues.put(COL_USER_PHONE, "13800138000"); // 手机号
        db.insert(TABLE_USER, null, userValues);
    }

    /**
     //Cursor：返回类型 (Return Type)
     //通过 Cursor，您可以：
     //移动指针：使用 moveToFirst(), moveToNext(), moveToPosition() 等方法遍历结果集中的每一行。
     //读取数据：使用 getString(), getInt(), getDouble() 等方法，根据列名或列索引，读取当前行中特定列的值。
     //重要提示：使用完 Cursor 后，必须调用 close() 方法来释放资源，否则会造成内存泄漏。通常在 Activity 的 onDestroy() 或使用 try-with-resources (如果 Cursor 实现了 AutoCloseable) 时进行。
     }
     */
    public Cursor getHealthRecords() {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库（系统自带函数，用法）
        //如果数据库已经存在，它会打开一个可读的数据库连接。
        //如果数据库不存在，它会先调用 onCreate() 方法创建数据库，然后再打开一个可读（实际上是可读写的）连接。
        //这个方法会返回一个 SQLiteDatabase 对象。
        // 查询健康表所有数据，按日期降序排列
        return db.query(TABLE_HEALTH, null, null, null, null, null, COL_HEALTH_DATE + " DESC");
        //db.query(...): 这是 SQLiteDatabase 类的一个便捷方法 (Convenience Method)，用于执行 SELECT 查询。它封装了 SQL SELECT 语句的构建。
        //return: 将 db.query() 方法的返回值（一个 Cursor 对象）直接作为 getHealthRecords() 方法的返回值。
        //db.query() 方法的参数详解（共7个）：
        //TABLE_HEALTH: 表名 (table)。指定要查询哪个表。这里是 health_records 表。
        //null: 要查询的列 (columns)。
            //如果传 null，表示查询表中的所有列（SELECT * FROM ...）。
            //如果只想查特定列，可以传一个字符串数组，例如 new String[]{COL_HEALTH_DATE, COL_HEALTH_WEIGHT}。
        //null: WHERE 子句 (selection)。
            //用于指定查询条件（WHERE 后面的部分，不包含 WHERE 关键字）。
            //传 null 表示没有条件，查询所有行（SELECT ... FROM table）。
            //例如，如果想查 weight > 3.0 的记录，可以传 "weight > ?"。
        //null: WHERE 子句的参数 (selectionArgs)。
            //如果 selection (第3个参数) 包含了占位符 ?，那么 selectionArgs 是一个字符串数组，用来为这些 ? 提供具体的值。
            //传 null，因为 selection 是 null。
            //例如，如果 selection 是 "weight > ?"，那么 selectionArgs 可以是 new String[]{"3.0"}。
        //null: 分组 (groupBy)。
            //对应 SQL 的 GROUP BY 子句。传 null 表示不进行分组。
            //null: HAVING 子句 (having)。
            //对应 SQL 的 HAVING 子句，通常与 GROUP BY 一起使用，用于过滤分组后的结果。传 null 表示没有 HAVING 条件。
        //COL_HEALTH_DATE + " DESC": 排序 (orderBy)。 指定结果集的排序方式。
            //在OrderBy位置：告诉系统依照那个值排序（也可以在这位置输入多个键，按逗号隔开，表明你要的排序主次）
            //COL_HEALTH_DATE 是列名常量（如 "date"）。
            //+ " DESC" 表示按该列进行降序 (Descending) 排列。DESC 是 SQL 关键字。
            //如果要升序排列，可以用 "ASC" 或省略（ASC 是默认值）。
            //例如，"date DESC, weight ASC" 表示先按日期降序，日期相同时再按体重升序。
    }

    /**
     * 添加一条健康记录
     * @param date 记录日期（格式"yyyy-MM-dd"）
     * @param weight 体重（kg）
     * @param temp 体温（℃）
     * @return 返回新插入记录的ID（-1表示插入失败）
     */
    public long addHealthRecord(String date, double weight, double temp) {
        //long:64位整型  date日期，weight体重，temp体温
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_HEALTH_DATE, date);
        values.put(COL_HEALTH_WEIGHT, weight);
        values.put(COL_HEALTH_TEMP, temp);
        // 插入数据并返回记录ID
        return db.insert(TABLE_HEALTH, null, values);
    }

    /**
     * 删除指定ID的健康记录
     * @param id 要删除的记录ID
     * @return 返回删除的行数（0表示删除失败）
     */
    public int deleteHealthRecord(long id) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        // 根据ID删除记录
        return db.delete(TABLE_HEALTH, COL_HEALTH_ID + " = ?", new String[]{String.valueOf(id)});
        //用删除函数，最终实际执行的等价于DELETE FROM health_records WHERE _id = COL_HEALTH_ID;一样效果
        //TABLE_HEALTH: 表名 (table)。指定要从哪个表中删除数据。这里是 health_records 表。
        //COL_HEALTH_ID + " = ?": WHERE 子句 (whereClause)。
        //这是一个字符串，定义了删除操作的条件（WHERE 后面的部分，不包含 WHERE 关键字）。
        //COL_HEALTH_ID 是列名常量（其值为 "_id"）。
        //" = ?" 是 SQL 条件的一部分，? 是一个占位符 (Placeholder)。
        //整个字符串 "_id = ?" 表示“删除 _id 列的值等于某个具体值的记录”。
        //使用 ? 占位符是关键：它能有效防止 SQL 注入 (SQL Injection) 攻击。如果直接拼接字符串（如 "_id = " + id），
        // 恶意输入可能会破坏 SQL 语句或执行非授权操作。使用占位符，数据库会安全地处理参数。
    }
    //new String[]{String.valueOf(id)}: WHERE 子句的参数 (whereArgs)。
    //这是一个字符串数组，用来为 whereClause 中的 ? 占位符提供具体的值。
    //String.valueOf(id): 将 long 类型的 id 参数转换成 String 类型（db.delete() 要求 whereArgs 是 String 数组）。
    //new String[]{...}: 创建一个包含这一个字符串元素的数组。
    //数组中元素的顺序对应 whereClause 中 ? 出现的顺序。这里只有一个 ?，所以数组也只有一个元素。
    //当执行删除时，数据库会将 ? 替换为 String.valueOf(id) 的值。



    /**
     * 获取所有商品列表
     * @return 返回包含所有商品的Cursor对象
     */
    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库
        // 查询所有商品
        return db.query(TABLE_PRODUCTS, null, null, null, null, null, null);
    }

    /**
     * 添加一个商品
     * @param name 商品名称
     * @param price 商品价格
     * @param desc 商品描述
     * @return 返回新插入商品的ID（-1表示插入失败）
     */
    public long addProduct(String name, double price, String desc) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_PRODUCT_PRICE, price);
        values.put(COL_PRODUCT_DESC, desc);
        // 插入商品并返回ID
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    /**
     * 获取所有论坛帖子
     * @return 返回包含所有帖子的Cursor对象，按ID倒序排列（最新的在前）
     */
    public Cursor getPosts() {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库
        // 查询所有帖子，按ID降序排列
        return db.query(TABLE_POSTS, null, null, null, null, null, COL_POST_ID + " DESC");
    }

    /**
     * 添加一个论坛帖子
     * @param title 帖子标题
     * @param author 作者用户名
     * @param content 帖子内容
     * @param views 初始浏览量
     * @return 返回新插入帖子的ID（-1表示插入失败）
     */
    public long addPost(String title, String author, String content, int views) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_POST_TITLE, title);
        values.put(COL_POST_AUTHOR, author);
        values.put(COL_POST_CONTENT, content);
        values.put(COL_POST_VIEWS, views);
        // 插入帖子并返回ID
        return db.insert(TABLE_POSTS, null, values);
    }

    /**
     * 删除指定ID的帖子
     * @param id 要删除的帖子ID
     * @return 返回删除的行数（0表示删除失败）
     */
    public int deletePost(long id) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        // 根据ID删除帖子
        return db.delete(TABLE_POSTS, COL_POST_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 获取用户信息
     * @return 返回包含用户信息的Cursor对象
     */
    public Cursor getUserInfo() {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库
        // 查询用户信息
        return db.query(TABLE_USER, null, null, null, null, null, null);
    }

    /**
     * 更新用户信息（覆盖所有现有记录）
     * @param name 用户名
     * @param email 电子邮箱
     * @param phone 手机号码
     * @return 返回更新的行数（0表示更新失败）
     */
    public int updateUserInfo(String name, String email, String phone) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PHONE, phone);
        // 更新所有用户信息（实际应用中通常按ID更新，此处简化逻辑）
        return db.update(TABLE_USER, values, null, null);
    }

    /**
     * 添加一条聊天消息
     * @param user 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     * @return 返回新插入消息的ID（-1表示插入失败）
     */
    public long addMessage(String user, String content, String time) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_MSG_USER, user);
        values.put(COL_MSG_CONTENT, content);
        values.put(COL_MSG_TIME, time);
        // 插入消息并返回ID
        return db.insert(TABLE_MESSAGES, null, values);
    }

    /**
     * 获取所有聊天消息
     * @return 返回包含所有消息的Cursor对象，按时间升序排列（最早的在前）
     */
    public Cursor getMessages() {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库
        // 查询所有消息，按时间升序排列
        return db.query(TABLE_MESSAGES, null, null, null, null, null, COL_MSG_TIME + " ASC");
    }   //记住Cursor对应返回db.query(类名，中间五个，OrderBy+“DESC或ACE”)

    /**
     * 删除指定ID的消息
     * @param id 要删除的消息ID
     * @return 返回删除的行数（0表示删除失败）
     */
    public int deleteMessage(long id) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        // 根据ID删除消息
        return db.delete(TABLE_MESSAGES, COL_MSG_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * 添加用户信息（用于首次保存用户资料）
     * @param name 用户名
     * @param email 电子邮箱
     * @param phone 手机号码
     * @return 返回新插入用户信息的ID（-1表示插入失败）
     */
    public long addUserInfo(String name, String email, String phone) {
        SQLiteDatabase db = getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, name);
        values.put(COL_USER_EMAIL, email);
        values.put(COL_USER_PHONE, phone);
        // 插入用户信息并返回ID
        return db.insert(TABLE_USER, null, values);
    }

    /**
     * 清空用户信息表所有数据
     * @return 返回删除的行数（0表示没有数据或删除失败）
     */
    public int clearUserInfo() {
        SQLiteDatabase db = getWritableDatabase(); // 获取可写数据库
        // 删除所有用户信息
        return db.delete(TABLE_USER, null, null);
        //示例用法：
        //删除 ID 为 5 的用户
        //db.delete(TABLE_USER, "id = ?", new String[]{"5"});
        //
        //// 删除名字为 "张三" 的所有用户
        //db.delete(TABLE_USER, "name = ?", new String[]{"张三"});
        //
        //// 删除年龄大于 60 的用户
        //db.delete(TABLE_USER, "age > ?", new String[]{"60"});
    }

    /**
     * 新增：注册用户账号
     * @param username 用户名（唯一）
     * @param password 密码（实际开发中建议加密后存储）
     * @return true表示注册成功，false表示失败（如用户名已存在）
     */
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // 获取可写数据库
        ContentValues values = new ContentValues();
        values.put(COL_ACCOUNT_USERNAME, username);
        values.put(COL_ACCOUNT_PASSWORD, password);
        // 插入账号信息，返回ID（-1表示失败，如用户名重复）
        long result = db.insert(TABLE_ACCOUNTS, null, values);
        return result != -1;
        //若result != -1为真，则返回true，插入成功
        //否则Flase
    }

    /**
     * 验证用户登录（检查用户名和密码是否匹配）
     * @param username 用户名
     * @param password 密码
     * @return true表示验证通过（登录成功），false表示失败
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // 获取可读数据库
        // 查询账号表中是否存在匹配的用户名和密码
        Cursor cursor = db.query(TABLE_ACCOUNTS,
                new String[]{COL_ACCOUNT_ID}, // 只查询ID字段（判断是否存在即可）
                COL_ACCOUNT_USERNAME + "=? AND " + COL_ACCOUNT_PASSWORD + "=?", // 条件：用户名和密码匹配
                new String[]{username, password}, // 条件参数
                null, null, null);
//COL_ACCOUNT_USERNAME + "=? AND " + COL_ACCOUNT_PASSWORD + "=?":
//这是 WHERE 子句的条件部分（不包括 WHERE 关键字本身）。
//它定义了查询的条件：用户名必须等于 ? 且密码必须等于 ?。
//COL_ACCOUNT_USERNAME 和 COL_ACCOUNT_PASSWORD 是代表列名的常量（如 "username", "password"）。
//使用 ? 作为占位符是防止 SQL 注入攻击的关键安全措施。
//new String[]{username, password}：给具体值供上面比较

        //boolean exists = cursor.getCount() > 0;:
        //如果有匹配的用户（用户名和密码都正确），getCount() 会大于 0（通常是 1，假设用户名唯一），表达式结果为 true。
        // 如果查询结果有数据，说明验证通过
        boolean exists = cursor.getCount() > 0;
        cursor.close(); // 关闭Cursor，避免内存泄漏
        return exists;  //返回结果，提示：正确还是不正确（有没有账号密码匹配）
    }
}