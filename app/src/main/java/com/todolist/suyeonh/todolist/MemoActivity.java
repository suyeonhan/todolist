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
import android.transition.ChangeImageTransform;
import android.transition.TransitionSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.todolist.suyeonh.todolist.Adapter.GroupRecyclerAdapter;
import com.todolist.suyeonh.todolist.models.Group;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MemoActivity extends AppCompatActivity {

    private static final String TAG = MemoActivity.class.getSimpleName();
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
                Intent intent = new Intent(MemoActivity.this, MemocreateActivity.class);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_memo, menu);
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                // 삭제를 누르면 확인을 받고 싶다
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("확인");
//                builder.setMessage("정말 삭제하시겠습니까");
//                builder.setIcon(R.mipmap.ic_launcher);
//                // 긍정 버튼
//                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        deleteMemo(info.id);
//                    }
//                });
//                // 부정 버튼
//                builder.setNegativeButton("취소", null);
//                builder.show();
//
//                return true;
//            case R.id.action_custom_dialog:
//                showCustomDialog();
//                return true;
//            default:
//                return super.onContextItemSelected(item);
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void showCustomDialog(final Long id) {
        //View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("그룹을 삭제합니다");
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

    private void deleteMemo(long id) {
        mRealm.beginTransaction();
        mRealm.where(Group.class).equalTo("id", id).findFirst().deleteFromRealm();
        mRealm.commitTransaction();
    }

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