package com.example.cookingguide.local

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cookingguide.model.FavouriteModel
import com.example.cookingguide.model.SeenModel
import com.example.cookingguide.model.filter.CategoryDetails


class InstructionsHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "cookguide.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_INSTRUCTION = "instructions"
        private const val TABLE_FAVOURITE = "favourite"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val querySeen =
            "CREATE TABLE $TABLE_INSTRUCTION ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idMeal VARCHAR (500) NOT NULL," +
                    "strMeal VARCHAR(500) NOT NULL," +
                    "strMealThumb VARCHAR (50) NOT NULL," +
                    "strCategoryThumb VARCHAR(50) NOT NULL)"
        val queryFavourite =
            "CREATE TABLE $TABLE_FAVOURITE ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "idMeal VARCHAR (500) NOT NULL," +
                    "strMeal VARCHAR(500) NOT NULL," +
                    "strMealThumb VARCHAR (50) NOT NULL," +
                    "strCategoryThumb VARCHAR(50) NOT NULL," +
                    "strCheck VARCHAR(10) NOT NULL)"
        p0?.execSQL(querySeen)
        p0?.execSQL(queryFavourite)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_INSTRUCTION")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVOURITE")
        onCreate(p0)
    }

    fun insertSeenIfNotExists(categorySeen: CategoryDetails) {
        if (!isDataExists(categorySeen.idMeal, TABLE_INSTRUCTION)) {
            insertSeen(categorySeen)
        }
    }

    fun insertFavouriteNotExists(favourite: FavouriteModel) {
        if (!isDataExists(favourite.idMeal, TABLE_FAVOURITE)) {
            insertFavourite(favourite)
        }
    }

    private fun insertSeen(seenModel: CategoryDetails) {
        val mDatabase = writableDatabase
        mDatabase.execSQL(
            "INSERT INTO '$TABLE_INSTRUCTION' (idMeal, strMeal, strMealThumb, strCategoryThumb) VALUES (?,?,?,?)",
            arrayOf(
                seenModel.idMeal,
                seenModel.strMeal,
                seenModel.strMealThumb,
                seenModel.strCategoryThumb
            )
        )
    }

    private fun insertFavourite(favourite: FavouriteModel) {
        val mDatabase = writableDatabase
        mDatabase.execSQL(
            "INSERT INTO '$TABLE_FAVOURITE' (idMeal, strMeal, strMealThumb, strCategoryThumb, strCheck) VALUES (?,?,?,?,?)",
            arrayOf(
                favourite.idMeal,
                favourite.strMeal,
                favourite.strMealThumb,
                favourite.strCategoryThumb,
                favourite.strCheck
            )
        )
    }

    private fun isDataExists(idMeals: String, table: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $table WHERE idMeal = ?"
        val cursor = db.rawQuery(query, arrayOf(idMeals))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun deleteDataFavourite(idMeal: String) {
        val db = writableDatabase
        db.execSQL("DELETE FROM '$TABLE_FAVOURITE' where idMeal = ?", arrayOf(idMeal))
    }

    @SuppressLint("Range")
    fun getCategorySeen(): ArrayList<SeenModel> {
        val categorySend = arrayListOf<SeenModel>()
        val selectQuery = "SELECT * FROM $TABLE_INSTRUCTION ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val idMeal = cursor.getString(cursor.getColumnIndex("idMeal"))
                val strMeal = cursor.getString(cursor.getColumnIndex("strMeal"))
                val strMealThumb = cursor.getString(cursor.getColumnIndex("strMealThumb"))
                val strCategoryThumb = cursor.getString(cursor.getColumnIndex("strCategoryThumb"))
                categorySend.add(SeenModel(idMeal, strMeal, strMealThumb, strCategoryThumb))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categorySend
    }

    @SuppressLint("Range")
    fun getDataFavourite(): ArrayList<FavouriteModel> {
        val favourite = arrayListOf<FavouriteModel>()
        val selectQuery = "SELECT * FROM $TABLE_FAVOURITE ORDER BY id DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val idMeal = cursor.getString(cursor.getColumnIndex("idMeal"))
                val strMeal = cursor.getString(cursor.getColumnIndex("strMeal"))
                val strMealThumb = cursor.getString(cursor.getColumnIndex("strMealThumb"))
                val strCategoryThumb = cursor.getString(cursor.getColumnIndex("strCategoryThumb"))
                val strCheck = cursor.getString(cursor.getColumnIndex("strCheck"))
                favourite.add(
                    FavouriteModel(
                        idMeal,
                        strMeal,
                        strMealThumb,
                        strCategoryThumb,
                        strCheck
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return favourite
    }

    @SuppressLint("Range")
    fun getDataByIdMeals(idMeal: String): String {
        var listCheck : String ?= null
        val db = this.readableDatabase
        val cursor =
            db.rawQuery(
                "SELECT strCheck FROM '$TABLE_FAVOURITE' WHERE idMeal = ?", arrayOf(
                    idMeal
                )
            )
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            listCheck = cursor.getString(0)
            cursor.moveToNext()
        }

        cursor.close()
        return listCheck.toString()
    }
}