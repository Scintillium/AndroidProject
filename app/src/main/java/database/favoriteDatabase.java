package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import classes.Question;

/**
 * Created by Ewan on 2016/12/22.
 */

public class favoriteDatabase extends SQLiteOpenHelper {
    private  static  final  int DATABASE_VERSION = 2 ;
    private  static  final String DATABASE_NAME = "FavoriteDatabase";
    private  static final String TABLE_NAME = "favoriteTable" ;
    private  static  final  String CREATE_TABLE = "CREATE TABLE if not exists " + TABLE_NAME + "(id TEXT PRIMARY KEY, title TEXT, body TEXT);" ;

    public favoriteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void insert(int id , String title , String body){
        SQLiteDatabase db = getWritableDatabase() ;
        ContentValues cv = new ContentValues();
        cv.put("id",String.valueOf(id));
        cv.put("title",title);
        cv.put("body",body) ;
        db.insert(TABLE_NAME,null,cv);
        Log.i("insert","success");
        db.close();
    }
    public void delete(int id){
        SQLiteDatabase db = getWritableDatabase() ;
        String whereClausell = "id = '" + String.valueOf(id) + "'";
        db.delete(TABLE_NAME,whereClausell,null);
        Log.i("delete","success");
        db.close();
    }
   public ArrayList<Question> query(){
       ArrayList<Question> questions = new ArrayList<>() ;
       SQLiteDatabase db = getWritableDatabase() ;
       Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
       while (cursor.moveToNext()){
           int id_index = cursor.getColumnIndex("id");
           int tiltle_index = cursor.getColumnIndex("title");
           int body_index = cursor.getColumnIndex("body");
           String id = cursor.getString(id_index);
           String title = cursor.getString(tiltle_index);
           String body = cursor.getString(body_index);
           Question question = new Question();
           question.setTitle(title);
           question.setId(Integer.parseInt(id));
           question.setBody(body);
           questions.add(question);
       }
       cursor.close();
       Log.i("Querry","success");
       return questions ;
   }

    public  boolean queryExistancce(int id){
        SQLiteDatabase db = getWritableDatabase() ;
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME + " where id = '" +  String.valueOf(id) +"';",null);
        if(cursor.getCount() == 0){
            cursor.close();
            Log.i("Existance","No");
            return false ;
        }
        else{
            Log.i("Existance","Yes");
            cursor.close();
            return true ;
        }
    }
}
