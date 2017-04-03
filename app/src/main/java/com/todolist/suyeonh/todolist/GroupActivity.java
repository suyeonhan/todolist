package com.todolist.suyeonh.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.todolist.suyeonh.todolist.adapter.GroupRecyclerAdapter;
import com.todolist.suyeonh.todolist.models.Group;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class GroupActivity extends AppCompatActivity {

    private static final String TAG = GroupActivity.class.getSimpleName();
    public static final int REQUEST_CODE_NEW_MEMO = 1000;
    public static final int REQUEST_CODE_UPDATE_MEMO = 1001;

    private List<Group> mGroupList;
    private GroupRecyclerAdapter mAdapter;
    private RecyclerView mMemoListView;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 화면 전환 기능 켜기
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet set = new TransitionSet();
            set.addTransition(new ChangeImageTransform());
            getWindow().setExitTransition(set);
            getWindow().setEnterTransition(set);

        }

//
//        SearchView searchView = (SearchView) findViewById(R.id.search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // 새로운 쿼리의 결과 뿌리기
//                List<Group> newMemoList = mMemoFacade.getMemoList(
//                        MemoContract.MemoEntry.COLUMN_NAME_TITLE + " LIKE '%" + newText + "%'",
//                        null,
//                        null,
//                        null,
//                        null
//                );
//                mAdapter.swap(newMemoList);
//
//                return true;
//            }
//        });

        mMemoListView = (RecyclerView) findViewById(R.id.memo_list);

        // 애니메이션 커스터마이징
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        animator.setChangeDuration(1000);
        mMemoListView.setItemAnimator(animator);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupActivity.this, MemoActivity.class);
                startActivity(intent);
            }
        });

        // 램
        mRealm = Realm.getDefaultInstance();

        RealmResults<Group> results = mRealm.where(Group.class).findAll();

        // 어댑터
        mAdapter = new GroupRecyclerAdapter(this, results);

        mMemoListView.setAdapter(mAdapter);

        // ContextMenu
        registerForContextMenu(mMemoListView);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            //스와이프 끝났을때 처리
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.delete(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(mMemoListView);
        mMemoListView.addItemDecoration(helper);
        mMemoListView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }



    // 새 그룹 추가 옵션 메뉴

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group:
                newGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //새 그룹 추가
    private void newGroup() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_input, null, false);
        final EditText groupEditText = (EditText) view.findViewById(R.id.text);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("새 그룹을 추가합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String title = groupEditText.getText().toString();

                // 신규
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Group group = mRealm.createObject(Group.class, Group.nextId(realm));
                        group.setTitle(title);
                    }
                });
            }
        });
        builder.setNegativeButton("취소", null);
        builder.setView(view);
        builder.show();
    }
}