package com.todolist.suyeonh.todolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.todolist.suyeonh.todolist.Utils.MyUtils;
import com.todolist.suyeonh.todolist.models.Memo;

import io.realm.Realm;

public class MemocreateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTitleEditText;
    private ImageView mImageView;

    private String mImagePath;

    private Realm mRealm;

    private long mId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo2);

        mImageView = (ImageView) findViewById(R.id.appbar_image);
        mTitleEditText = (EditText) findViewById(R.id.title_edit);
        mRealm = Realm.getDefaultInstance();

        findViewById(R.id.toolbar_layout).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 타이틀 없애기
        getSupportActionBar().setTitle("");

        if (getIntent() != null) {
            if (getIntent().hasExtra("memo")) {
                // 보여주기

//                mId = getIntent().getLongExtra("id", -1);

                Memo memo = (Memo) getIntent().getParcelableExtra("memo");
                mTitleEditText.setText(memo.getTitle());
                mImagePath = memo.getImagePath();
                if (mImagePath != null) {
                    Glide.with(this).load(mImagePath).into(mImageView);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_memo2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
            case R.id.action_cancel:
                cancel();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void save() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = mRealm.createObject(Memo.class);
                memo.setTitle(mTitleEditText.getText().toString());
                memo.setImagePath(mImagePath);

                Log.d("DB", "execute: " + mRealm.where(Memo.class).findAll().toString());
                finish();
            }
        });

    }

    public void onImageClick2(View view) {
        // 그림 줘
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK && data != null) {
            // 그림이 정상적으로 선택되었을 때

            // 사진 경로
            Uri uri = data.getData();

            mImagePath = MyUtils.getRealPath(this, uri);

            // 라이브러리
            Glide.with(this).load(mImagePath).into(mImageView);
        }
    }

    @Override
    public void onClick(View v) {
        onImageClick2(v);
    }
}