package com.todolist.suyeonh.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.todolist.suyeonh.todolist.Utils.MyUtils;
import com.todolist.suyeonh.todolist.adapter.MemoRecyclerAdapter;
import com.todolist.suyeonh.todolist.models.Group;
import com.todolist.suyeonh.todolist.models.Memo;

import org.greenrobot.eventbus.Subscribe;

import io.realm.Realm;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mMemoEditText;
    private ImageView mImageView;
    private SearchView mSearchView;

    private String mImagePath;

    private Realm mRealm;

    private long mId = -1;
    private Group mGroup;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_create);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        mImageView = (ImageView) findViewById(R.id.appbar_image);
        mMemoEditText = (EditText) findViewById(R.id.title_edit);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyler_view);

        mRealm = Realm.getDefaultInstance();

        findViewById(R.id.toolbar_layout).setOnClickListener(this);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // 서치뷰의 내용으로 검색을 수행할 때 호출 됨
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 서치뷰의 자가 변경될 때마다 호출 됨
//                Log.d("MainActivity", "onQueryTextChange: " + newText);
//                return false;
//            }
//        });

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


        MemoRecyclerAdapter adapter = new MemoRecyclerAdapter(this, mGroup.getMemoList());
        mRecyclerView.setAdapter(adapter);

    }

    // 삭제 팝업

    @Subscribe
    public void showCustomDialog(final Long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("메모를 삭제합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMemo(id);
            }
        });
        builder.setNegativeButton("취소", null);
        //builder.setView(view);
        builder.show();
    }

    // 메모 삭제
    private void deleteMemo(long id) {
        mRealm.beginTransaction();
        mRealm.where(Memo.class).equalTo("id", id).findFirst().deleteFromRealm();
        mRealm.commitTransaction();
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