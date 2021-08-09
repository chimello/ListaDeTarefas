package com.example.listadetarefas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listadetarefas.R;
import com.example.listadetarefas.model.Tarefa;

import java.text.SimpleDateFormat;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.MyViewHolder>{

    private List<Tarefa> listaTarefas;

    public TarefaAdapter(List<Tarefa> lista) {
        this.listaTarefas = lista;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.lista_tarefa_adapter, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Tarefa tarefa = listaTarefas.get(position);
        holder.tarefa.setText(tarefa.getNomeTarefa());
        String dataVencimento;
        if (tarefa.getDataVencimento() == null) {
            dataVencimento = "Sem Vencimento";
        } else {
            dataVencimento = String.format(tarefa.getDataVencimento(), "dd/MM/yyyy HH:mm");

        }
        holder.vencimento.setText("Vencimento: " + dataVencimento);
    }

    @Override
    public int getItemCount() {

        return this.listaTarefas.size(); //quantidade de itens
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tarefa, vencimento;

        public MyViewHolder(View itemView) {
            super(itemView);

            tarefa = itemView.findViewById(R.id.textTarefa);
            vencimento = itemView.findViewById(R.id.vencimento);
        }
    }


}
