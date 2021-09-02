package com.example.listadetarefas.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.listadetarefas.R;
import com.example.listadetarefas.helper.TarefaDAO;
import com.example.listadetarefas.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;
    private String status;
    EditText vencimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);
        editTarefa = findViewById(R.id.textTarefa);
        editTarefa.requestFocus();
        vencimento = findViewById(R.id.vencimento);

        vencimento.setInputType(InputType.TYPE_NULL);

        vencimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(vencimento);
            }
        });

        //recuperar a tarefa caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");
        //Configurar a caixa de texto
        if (tarefaAtual != null) {
            editTarefa.setText(tarefaAtual.getNomeTarefa());
            vencimento.setText(tarefaAtual.getDataVencimento());
            status = tarefaAtual.getStatus();

        }
    }

    private void showDateTimeDialog(EditText vencimento) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        vencimento.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(AdicionarTarefaActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(AdicionarTarefaActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static String getcurrentDateAndTime(){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSalvar:
                //executa a ação de salvar
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                if (tarefaAtual != null) { //alterar

                    String nomeTarefa = editTarefa.getText().toString();
                    String dataVencimento;
                    if (vencimento.getText().toString().equals("")) {
                        dataVencimento = getcurrentDateAndTime();
                    } else {
                        dataVencimento = vencimento.getText().toString();
                    }
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());
                        tarefa.setStatus(tarefaAtual.getStatus());
                        tarefa.setDataCadastro(tarefa.getDataCadastro());
                        tarefa.setDataVencimento(dataVencimento);
                        tarefa.setStatus("A");

                        //atualizar o banco
                        if (tarefaDAO.atualizar(tarefa)) {
                            finish();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Tarefa Atualizada Com Sucesso!",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Erro ao Atualizar Tarefa!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }

                } else { //salvar

                    String nomeTarefa = editTarefa.getText().toString();
                    String dataVencimento;
                    if (vencimento.getText().toString().equals("")) {
                        dataVencimento = getcurrentDateAndTime();
                    } else {
                        dataVencimento = vencimento.getText().toString();
                    }
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setStatus("A");
                        tarefa.setDataCadastro(getcurrentDateAndTime());
                        tarefa.setDataVencimento(getcurrentDateAndTime()/*dataVencimento*/);
                        if ( tarefaDAO.salvar(tarefa)) {
                            finish();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Tarefa Salva Com Sucesso!",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Erro Ao Salvar Tarefa!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}