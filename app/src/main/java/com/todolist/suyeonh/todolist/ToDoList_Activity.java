package com.todolist.suyeonh.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.todolist.suyeonh.todolist.Adapter.Myadapter;

public class ToDoList_Activity extends AppCompatActivity {

    private TextView mTextView;
    private ProgressBar mProgressBar;
    private CardView mCardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyler_view);
        mTextView = (TextView) findViewById(R.id.card_text);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
        mCardview = (CardView) findViewById(R.id.card_view);


        String[] dataSet = {"할일1", "할일2", "할일3"};

        recyclerView.setAdapter(new Myadapter(dataSet));

    }

    //메뉴 붙이기
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todolist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create:
                Toast.makeText(this, "새 그룹 추가", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
