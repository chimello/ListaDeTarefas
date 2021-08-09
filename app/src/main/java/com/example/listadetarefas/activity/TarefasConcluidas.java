package com.example.listadetarefas.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.listadetarefas.R;
import com.example.listadetarefas.adapter.TarefaAdapter;
import com.example.listadetarefas.helper.RecyclerItemClickListener;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefasConcluidas extends AppCompatActivity {

    private RecyclerView recyclerTarefasConcluidas;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas_concluidas);

        recyclerTarefasConcluidas = findViewById(R.id.recyclerConcluidas);

        recyclerTarefasConcluidas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerTarefasConcluidas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) { //para alterar os itens

                                AlertDialog.Builder dialog = new AlertDialog.Builder(TarefasConcluidas.this);
                                dialog.setTitle("Selecionar Ação");
                                dialog.setMessage("Deseja Editar ou concluir a tarefa?");
                                dialog.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //recupera item para edição
                                        Tarefa tarefaSelecionada = listaTarefas.get(position);
                                        //envia a tarefa
                                        Intent intent = new Intent(TarefasConcluidas.this, AdicionarTarefaActivity.class);
                                        intent.putExtra("tarefaSelecionada", tarefaSelecionada);
                                        startActivity(intent);
                                    }
                                });
                                dialog.setNegativeButton("Reabrir", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                                        //recuperar a tarefa para concluir
                                        tarefaSelecionada = listaTarefas.get(position);
                                        if (tarefaDAO.reabrir(tarefaSelecionada)) {
                                            carregarListaTarefasConcluidas();
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

                                AlertDialog.Builder dialog = new AlertDialog.Builder(TarefasConcluidas.this);

                                //configura titulo e msg
                                dialog.setTitle("Confirmar Exclusão");
                                dialog.setMessage("Deseja Excluir a Tarefa: " + tarefaSelecionada.getNomeTarefa() + " ?");
                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        if (tarefaDAO.concluir(tarefaSelecionada)) {
                                            carregarListaTarefasConcluidas();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarListaTarefasConcluidas();

    }

    public void carregarListaTarefasConcluidas() {
        //listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listarConcluidas();

        //configura o Adapter
        tarefaAdapter = new TarefaAdapter(listaTarefas);

        //Configura o recyblerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTarefasConcluidas.setLayoutManager(layoutManager);
        recyclerTarefasConcluidas.setHasFixedSize(true);
        recyclerTarefasConcluidas.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerTarefasConcluidas.setAdapter(tarefaAdapter);
    }
}