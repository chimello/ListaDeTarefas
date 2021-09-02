package com.example.listadetarefas.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 2;
    public static String NOME_DB = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";
    private List<String> CAMPOS_TABELAS;

    public DbHelper(Context context) {

        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS +
                     " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     " nome TEXT NOT NULL, " +
                     "status VARCHAR(1), " +
                     "dataCadastro DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                     "dataVencimento DATETIME); ";

        try {
            db.execSQL(sql);
            Log.i("info DB", "Sucesso ao criar a tabela");
        } catch (Exception e) {
            Log.i("info DB", "Erro ao criar Tabela" + e.getMessage());
        }

    }

    //será utilizado qnd o app já estiver instalado e ele precisa verificar os campos do banco
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql1 = "ALTER TABLE " + TABELA_TAREFAS + " ADD dataVencimento DATETIME;";
        String sql2 = "ALTER TABLE " + TABELA_TAREFAS + " ADD dataCadastro DATETIME;";

        try {
            db.execSQL(sql1);
            db.execSQL(sql2);
            //onCreate(db);
            Log.i("info DB", "Sucesso ao Atualizar App");
        } catch (Exception e) {
            Log.i("info DB", "Erro ao Atualizar App" + e.getMessage());
        }

    }
}
