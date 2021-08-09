package com.example.listadetarefas.helper;

import com.example.listadetarefas.model.Tarefa;

import java.util.List;

public interface iTarefaDAO {

    public Boolean salvar(Tarefa tarefa);
    public Boolean atualizar(Tarefa tarefa);
    public Boolean deletar(Tarefa tarefa);
    public Boolean concluir(Tarefa tarefa);
    public List<Tarefa> listar();
}
