package com.example.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.listadetarefas.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetarefas.adapter.TarefaAdapter;
import com.example.listadetarefas.databinding.ActivityMainBinding;
import com.example.listadetarefas.helper.DbHelper;
import com.example.listadetarefas.helper.RecyclerItemClickListener;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //configura o RecyclerView
        recyclerView = findViewById(R.id.recyclerListaTarefas);

        //adicionando evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) { //para alterar os itens

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setTitle("Selecionar Ação");
                                dialog.setMessage("Deseja Editar ou concluir a tarefa?");
                                dialog.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //recupera item para edição
                                        Tarefa tarefaSelecionada = listaTarefas.get(position);
                                        //envia a tarefa
                                        Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("Concluir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                                        //recuperar a tarefa para concluir
                                        tarefaSelecionada = listaTarefas.get(position);
                                        if (tarefaDAO.concluir(tarefaSelecionada)) {
                                            carregarListaTarefas();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Sucesso Ao Concluir Tarefa!",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        } else {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Erro ao Concluir tarefa!",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                                });

                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) { //para remover os itens

                                //recuperar a tarefa para deletar
                                tarefaSelecionada = listaTarefas.get(position);

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                                //configura titulo e msg
                                dialog.setTitle("Confirmar Exclusão");
                                dialog.setMessage("Deseja Excluir a Tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?");
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        if (tarefaDAO.deletar(tarefaSelecionada)) {
                                            carregarListaTarefas();
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Sucesso Ao Excluir Tarefa!",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        } else {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    "Erro ao Excluir tarefa!",
                                                    Toast.LENGTH_LONG
                                            ).show();
                                        }
                                    }
                                });
                                dialog.setNegativeButton("Não", null);

                                //exibir a dialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity(intent);
            }
        });
    }

    public void carregarListaTarefas() {
        //listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        //configura o Adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);

        //Configura o recyblerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);
    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tarefasConcluidas) {
            Intent intent = new Intent(getApplicationContext(), TarefasConcluidas.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}