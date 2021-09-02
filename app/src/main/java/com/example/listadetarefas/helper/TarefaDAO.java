package com.example.listadetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.listadetarefas.model.Tarefa;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements iTarefaDAO {

    private SQLiteDatabase escreve;
    private SQLiteDatabase ler;

    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        ler = db.getReadableDatabase();
    }

    @Override
    public Boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        cv.put("status", tarefa.getStatus());
        cv.put("dataCadastro", String.valueOf(tarefa.getDataCadastro()));
        cv.put("dataVencimento", String.valueOf(tarefa.getDataVencimento()));
        if (tarefa.getDataVencimento() == null) {
            tarefa.setDataVencimento(String.valueOf(tarefa.getDataCadastro()));
        } else {
            tarefa.getDataVencimento();
        }

        try {
            escreve.insert(DbHelper.TABELA_TAREFAS, null, cv);
            Log.i("INFO", "Tarefa Salva com sucesso!");
        } catch (Exception e) {
            Log.i("INFO", "Erro ao salvar tarefa: " + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public Boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());
        cv.put("status", tarefa.getStatus());
        cv.put("dataCadastro", tarefa.getDataCadastro());
        cv.put("dataVencimento", tarefa.getDataVencimento());
        try {
            String[] args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS, cv, "id=?", args);
            Log.i("INFO", "Tarefa Atualizada com Sucesso!");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao Atualizar Tarefa" + e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean deletar(Tarefa tarefa) {

        try {
            String[] args = {tarefa.getId().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS, "id=?", args);
            Log.i("INFO", "Tarefa Removida com Sucesso!");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao Remover Tarefa" + e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefa> listar() {

        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE status != 'C' ORDER BY dataVencimento ASC;";
        Cursor c = ler.rawQuery(sql, null);

        //c.moveToFirst();
        while (c.moveToNext()) {
            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));
            String dataCadastro = c.getString(c.getColumnIndex("dataCadastro"));
            String dataVencimento = c.getString(c.getColumnIndex("dataVencimento"));
            String status = c.getString(c.getColumnIndex("status"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);
            tarefa.setDataCadastro(dataCadastro);
            tarefa.setDataVencimento(dataVencimento);
            tarefa.setStatus(status);

            tarefas.add(tarefa);
        }
        return tarefas;

    }

    public List<Tarefa> listarConcluidas() {

        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE status = 'C' ORDER BY dataVencimento ASC;";
        Cursor c = ler.rawQuery(sql, null);

        //c.moveToFirst();
        while (c.moveToNext()) {
            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));
            String status = c.getString(c.getColumnIndex("status"));
            String dataCadastro = c.getString(c.getColumnIndex("dataCadastro"));
            String dataVencimento = c.getString(c.getColumnIndex("dataVencimento"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nomeTarefa);
            tarefa.setStatus(status);
            tarefa.setDataCadastro(dataCadastro);
            tarefa.setDataVencimento(dataVencimento);

            tarefas.add(tarefa);
        }
        return tarefas;

    }

    public Boolean concluir(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("status", "C");
        try {
            String[] args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS, cv, "id=?", args);
            Log.i("INFO", "Tarefa Concluída com Sucesso!");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao Concluir Tarefa" + e.getMessage());
        }
        return true;
    }

    public Boolean reabrir(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("status", "A");
        try {
            String[] args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS, cv, "id=?", args);
            Log.i("INFO", "Tarefa Concluída com Sucesso!");
        } catch (Exception e) {
            Log.e("INFO", "Erro ao Concluir Tarefa" + e.getMessage());
        }
        return true;
    }

    //DAO = Data Acess Object

}
