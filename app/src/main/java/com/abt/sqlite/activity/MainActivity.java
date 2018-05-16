package com.abt.sqlite.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.abt.sqlite.helper.DBHelper;
import com.abt.sqlite.R;
import com.abt.sqlite.bean.User;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private EditText etUserName,etPassword;
    private ArrayList<User> alUser;
    //private ListView lvUser;
    //private UserAdapter adapter;
    private TextView tvPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSoftKeyboard();
        initView();
        initData();
    }

    private void initView() {
        DBHelper databaseHelper=new DBHelper(MainActivity.this);
        db=null;
        db=databaseHelper.getReadableDatabase();

        etUserName=(EditText) findViewById(R.id.et_username);
        etPassword=(EditText) findViewById(R.id.et_password);
        //lvUser=(ListView) findViewById(R.id.lv_user);

        alUser=new ArrayList<>();
       /* adapter=new UserAdapter(this,alUser);
        lvUser.setAdapter(adapter);
        lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etUserName.setText(alUser.get(position).username);
                etUserName.setSelection(etUserName.length());
                etPassword.setText(alUser.get(position).password);
            }
        });*/

        //新增
        findViewById(R.id.bt_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this,getString(R.string.can_not_be_empty),Toast.LENGTH_SHORT).show();
                } else {
                    String sql = "insert into user(username,password) values ('" + etUserName.getText().toString().trim() + "','" + etPassword.getText().toString().trim() + "')";
                    db.execSQL(sql);
                    initData();
                }
            }
        });

        //删除
        findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String sql="delete from user where username='"+etUserName.getText().toString().trim()+"'";
//                db.execSQL(sql);

                String whereClause="username=?";
                String[] whereArgs={etUserName.getText().toString().trim()};
                db.delete("user",whereClause,whereArgs);
                initData();
            }
        });

        //修改
        findViewById(R.id.bt_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql="update user set password='"+etPassword.getText().toString().trim()+"' where username='"+etUserName.getText().toString().trim()+"'";
                db.execSQL(sql);
                initData();
            }
        });
    }

    //查询
    private void initData() {
        alUser.clear();
        Cursor cursor = db.query("user",null,null,null,null,null,null);//查询并获得游标
        while ((cursor.moveToNext())){
            User user=new User();
            user.username=cursor.getString(cursor.getColumnIndex("username"));
            user.password=cursor.getString(cursor.getColumnIndex("password"));
            alUser.add(user);
        }
        //adapter.notifyDataSetChanged();
        etUserName.setText("");
        etUserName.requestFocus();
        etPassword.setText("");
    }

    //隐藏软键盘
    public void hideSoftKeyboard(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
