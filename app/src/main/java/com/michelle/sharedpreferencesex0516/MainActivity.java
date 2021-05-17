package com.michelle.sharedpreferencesex0516;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private EditText ed_pre;
    private TextView tvAccountError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_pre = findViewById(R.id.Pre);
        button = findViewById(R.id.button);
        tvAccountError = findViewById(R.id.tv_account_error);
        Intent intent = new Intent(MainActivity.this, Store.class);
        //預設鎖住按鈕
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        ed_pre.addTextChangedListener(idCardWatcher);
    }

    private TextWatcher idCardWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            temp = s;
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before,int count) {
            //先判斷是否10碼，再判斷是否為身分證格式
            if (ed_pre.length() == 10) {
                if(isIdNoFormat(temp.toString())) {
                    button.setEnabled(true);
                    button.setBackground(getResources().getDrawable(R.drawable.login_b_unlock));
                    tvAccountError.setVisibility(View.GONE);
                } else {
                    tvAccountError.setVisibility(View.VISIBLE);
                }
            } else {
                button.setEnabled(false);
                button.setBackground(getResources().getDrawable(R.drawable.login_b_lock));
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
            //身分證字號輸入英文字自動大寫。
            // 先移除當前監聽,避免死迴圈。
            ed_pre.removeTextChangedListener(this);
            String string = ed_pre.getText().toString().toUpperCase();
            ed_pre.setText(string); ed_pre.setSelection(string.length());
            // 讓游標定位最後位置。
            // 操作完當前顯示內容之後,再新增監聽。
            ed_pre.addTextChangedListener(this);
        }

        //判斷身份證字號格式
        public boolean isIdNoFormat(String word) {
            boolean check = true;

            for(int i = 0 ; i < word.length(); i++) {
                String s = word.substring(i, i+1);
                if(i == 0) {
                    if(! isEnglish(s)) {
                        check = false;
                        break;
                    }
                } else if (i == 1) {
                    if (! (s.equals("1")) && ! (s.equals("2"))) {
                        check = false;
                        break;
                    }
                } else {
                    if(! isDigit(s)) {
                        check = false;
                        break;
                    }
                }
            }

            return check;
        }

        //判斷第一碼是否為英文
        public boolean isEnglish(String word) {
            boolean check = true;
            for(int i = 0; i < word.length(); i++) {
                int c = (int)word.charAt(i);
                if(c >= 65 && c <= 90 || c >= 97 && c <= 122) {
                    //是英文
                } else {
                    check = false;
                }
            }
            return check;
        }

        //判斷第二碼 ~ 最後一碼是否為數字 (第二碼為 1 or 2)
        public boolean isDigit(String word) {
            boolean check = true;
            for(int i=0;i<word.length();i++) {
                int c = (int)word.charAt(i);
                if(c>=48 && c<=57 ) {
                    //是數字
                } else {
                    check = false;
                }
            }
            return check;
        }

    };
}