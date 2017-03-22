package com.todolist.suyeonh.todolist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.todolist.suyeonh.todolist.adapter.MemoRecyclerAdapter;
import com.todolist.suyeonh.todolist.Utils.MyUtils;
import com.todolist.suyeonh.todolist.models.Group;
import com.todolist.suyeonh.todolist.models.Memo;

import io.realm.Realm;

public class MemocreateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mMemoEditText;
    private ImageView mImageView;

    private String mImagePath;

    private Realm mRealm;

    private long mId = -1;
    private Group mGroup;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo2);

        mImageView = (ImageView) findViewById(R.id.appbar_image);
        mMemoEditText = (EditText) findViewById(R.id.title_edit);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyler_view);

        mRealm = Realm.getDefaultInstance();

        findViewById(R.id.toolbar_layout).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 타이틀 없애기
        getSupportActionBar().setTitle("");

        if (getIntent() != null) {
            if (getIntent().hasExtra("id")) {
                // 보여주기
                mId = getIntent().getLongExtra("id", -1);

                mGroup = mRealm.where(Group.class).equalTo("id", mId).findFirst();

                mImagePath = mGroup.getImagePath();
                if (mImagePath != null) {
                    Glide.with(this).load(mImagePath).into(mImageView);
                }
            }
        }

        mMemoEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!event.isShiftPressed()) {
                        // the user is done typing.
                        // 메모 저장
                        mRealm.beginTransaction();
                        Memo memo = mRealm.createObject(Memo.class);
                        memo.setMemo(mMemoEditText.getText().toString());
                        mGroup.getMemoList().add(memo);
                        mMemoEditText.setText("");
                        mRealm.commitTransaction();
                        return true; // consume.
                    }
                }
                return false; // pass on to other listeners.
            }
        });


        MemoRecyclerAdapter adapter = new MemoRecyclerAdapter(mGroup.getMemoList());
        mRecyclerView.setAdapter(adapter);

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

            mRealm.beginTransaction();
            mGroup.setImagePath(mImagePath);
            mRealm.commitTransaction();
        }
    }

    @Override
    public void onClick(View v) {
        onImageClick2(v);
    }
}