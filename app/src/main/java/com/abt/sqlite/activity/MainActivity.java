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

/**
 * @描述： @MainActivity
 * @作者： @黄卫旗
 * @创建时间： @2018/5/16
 */
public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase sqLiteDatabase;
    private EditText etUserName, etPassword;
    private ArrayList<User> alUser;
    private TextView tvPosition;
    private TextView tvContent;
    private StringBuilder mStringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hideSoftKeyboard();
        initView();
        initData();
    }

    private void initView() {
        DBHelper databaseHelper = new DBHelper(MainActivity.this);
        sqLiteDatabase = null;
        sqLiteDatabase = databaseHelper.getReadableDatabase();

        tvContent  = (TextView) findViewById(R.id.content);
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        alUser = new ArrayList<>();

        findViewById(R.id.bt_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this,getString(R.string.can_not_be_empty),Toast.LENGTH_SHORT).show();
                } else {
                    String name = etUserName.getText().toString().trim();
                    String passwd = etPassword.getText().toString().trim();
                    String sql = "insert into user(username,password) values ('" + name + "','" + passwd + "')";
                    sqLiteDatabase.execSQL(sql);
                    initData();
                }
            }
        });

        findViewById(R.id.bt_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String sql="delete from user where username='"+etUserName.getText().toString().trim()+"'";
                sqLiteDatabase.execSQL(sql);*/
                String whereClause="username=?";
                String[] whereArgs={etUserName.getText().toString().trim()};
                sqLiteDatabase.delete("user",whereClause,whereArgs);
                initData();
            }
        });

        findViewById(R.id.bt_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etUserName.getText().toString().trim();
                String passwd = etPassword.getText().toString().trim();
                String sql="update user set password='"+passwd+"' where username='"+name+"'";
                sqLiteDatabase.execSQL(sql);
                initData();
            }
        });

        findViewById(R.id.bt_transaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTransaction();
                initData();
            }
        });
    }

    private void initData() {
        mStringBuilder.delete(0, mStringBuilder.length()); // 清空
        alUser.clear(); // 查询并获得游标
        Cursor cursor = sqLiteDatabase.query("user",null,null,null,null,null,null);
        while ((cursor.moveToNext())) {
            User user=new User();
            user.username = cursor.getString(cursor.getColumnIndex("username"));
            user.password = cursor.getString(cursor.getColumnIndex("password"));
            alUser.add(user);
            mStringBuilder.append("username="+user.username+", password="+user.password+"\n");
            tvContent.setText(mStringBuilder.toString());
        }
        etUserName.setText("");
        etUserName.requestFocus();
        etPassword.setText("");
    }

    public void hideSoftKeyboard() { // 隐藏软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void doTransaction() {
        DBHelper databaseHelper = new DBHelper(MainActivity.this);
        sqLiteDatabase = null;
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        // 开启事务
        sqLiteDatabase.beginTransaction();
        try {
            String name = etUserName.getText().toString().trim();
            String passwd = etPassword.getText().toString().trim();
            String sql = "insert into user(username,password) values ('" + name + "','" + passwd + "')";
            sqLiteDatabase.execSQL(sql);

            passwd += "_append";
            String sqlUpdate="update user set password='"+passwd+"' where username='"+name+"'";
            sqLiteDatabase.execSQL(sqlUpdate);

            // 设置事务标志为成功，当结束事务时就会提交事务
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            //结束事务
            sqLiteDatabase.endTransaction();
        }
    }

}
