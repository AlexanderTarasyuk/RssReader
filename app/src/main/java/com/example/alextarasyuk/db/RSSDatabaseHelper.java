package com.example.alextarasyuk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alextarasyuk.model.Website;

import java.util.ArrayList;
import java.util.List;

public class RSSDatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASEVERSION = 1;
    private static final String DATABASENAME = "rssReader";
    private static final String TABLE_RSS = "websites";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_RSS_LINK = "rss_link";
    private static final String KEY_DESCRIPTION = "description";


    public RSSDatabaseHelper(Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_RSS + "(" + KEY_ID
                + "INTEGER PRIMARY KEY," + KEY_TITLE + "TEXT," + KEY_LINK
                + " TEXT," + KEY_RSS_LINK + " TEXT," + KEY_DESCRIPTION
                + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_RSS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS);

        onCreate(sqLiteDatabase);

    }

    public Website getSite(int id) {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_RSS,
                new String[]{KEY_ID, KEY_TITLE, KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if (cursor != null) cursor.moveToFirst();

        Website website = new Website(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));

        website.setId(Integer.parseInt(cursor.getString(0)));
        website.setTitle(cursor.getString(1));
        website.setLink(cursor.getString(2));
        website.setRSSLink(cursor.getString(3));
        website.setDescription(cursor.getString(4));
        cursor.close();
        database.close();
        return website;
    }

    public void deleteSite(Website website) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RSS,
                KEY_ID + "=?",
                new String[]{String.valueOf(website.getId())});
        sqLiteDatabase.close();
    }

    public boolean isSiteExists(SQLiteDatabase database, String rss_link) {
        Cursor cursor = database.rawQuery("SELECT 1 FROM " + TABLE_RSS + "WHERE rss_link= '"
                + rss_link
                + "'", new String[]{});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

    public List<Website> getAllSites() {
        List<Website> siteList = new ArrayList<Website>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RSS
                + " ORDER BY id DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Website site = new Website();
                site.setId(Integer.parseInt(cursor.getString(0)));
                site.setTitle(cursor.getString(1));
                site.setLink(cursor.getString(2));
                site.setRSSLink(cursor.getString(3));
                site.setDescription(cursor.getString(4));
                // Adding contact to list
                siteList.add(site);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return contact list
        return siteList;
    }

    public void addSite(Website site) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle()); // site title
        values.put(KEY_LINK, site.getLink()); // site url
        values.put(KEY_RSS_LINK, site.getRSSLink()); // rss link url
        values.put(KEY_DESCRIPTION, site.getDescription()); // site description

        // Check if row already existed in database
        if (!isSiteExists(db, site.getRSSLink())) {
            // site not existed, create a new row
            db.insert(TABLE_RSS, null, values);
            db.close();
        } else {
            // site already existed update the row
            updateSite(site);
            db.close();
    }


}
    public int updateSite(Website site) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle());
        values.put(KEY_LINK, site.getLink());
        values.put(KEY_RSS_LINK, site.getRSSLink());
        values.put(KEY_DESCRIPTION, site.getDescription());

        // updating row return
        int update = db.update(TABLE_RSS, values, KEY_RSS_LINK + " = ?",
                new String[] { String.valueOf(site.getRSSLink()) });
        db.close();
        return update;

    }}

