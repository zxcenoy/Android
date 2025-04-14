package com.example.lab39android;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TodoAdapter todoAdapter;
    private RecyclerView recyclerView;
    ArrayList<TODO> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = new ArrayList<>();
        recyclerView = findViewById(R.id.Rec);
        readItems();
        todoAdapter = new TodoAdapter(this,todoList, new TodoAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                createOrEditTask(position);
            }

            @Override
            public void onItemLongClick(int position) {
                todoList.remove(position);
                todoAdapter.notifyItemRemoved(position);
                writeItems();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(todoAdapter);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                todoAdapter.getFilter().filter(query);
                return  false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todoList.txt");
        try {
            List<String> lines = new ArrayList<>();
            for (TODO item : todoList){
                lines.add(TODO.toString(item));
            }
            FileUtils.writeLines(todoFile,lines);
        }
        catch (IOException e){
            Log.e("writeitems",e.getLocalizedMessage());
        }
    }
    private void readItems(){
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todolist.txt");
        try{
            List<String> lines = FileUtils.readLines(todoFile,String.valueOf(Charset.defaultCharset()));
            todoList.clear();
            for (String line : lines){
                todoList.add(TODO.fromString(line));
            }
        }
        catch (IOException e){
            todoList = new ArrayList<>();
        }
    }

    public void onAddItem(View v){
        createOrEditTask(-1);
    }

    private void createOrEditTask(final int pos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(pos == -1? "Создать задачу" : "Редактировать задачу");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText inputTitle = new EditText(this);
        inputTitle.setHint("Заголовок");
        final EditText inputText = new EditText(this);
        inputText.setHint("Описание");
        if (pos != -1){
            inputTitle.setText(todoList.get(pos).getTitle());
            inputText.setText(todoList.get(pos).getText());
        }
        layout.addView(inputTitle);
        layout.addView(inputText);
        builder.setView(layout);

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = inputTitle.getText().toString();
                String text = inputText.getText().toString();
                if (pos == -1){
                    todoList.add(new TODO(title,text));
                    todoAdapter.notifyItemChanged(pos);
                }
                else {
                    todoList.set(pos, new TODO(title,text));
                    todoAdapter.notifyItemChanged(pos);
                }
                writeItems();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
}