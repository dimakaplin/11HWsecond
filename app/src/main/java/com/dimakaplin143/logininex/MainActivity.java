package com.dimakaplin143.logininex;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean exSave;

    private CheckBox exSaveBox;

    private Button okBtn;
    private Button regBtn;

    private EditText editLogin;
    private EditText editPassword;

    private StorageControl storage;

    private final String LOG_PASS = "log_pass";
    public static final String EX_SAVE = "ex_save";

    private String message;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = new StorageControl(this);
        checkExSave();
        initViews();


    }

    public void checkExSave() {
        exSave = storage.getSharedBool(EX_SAVE);
    }

    public void initViews() {
        okBtn = findViewById(R.id.ok_btn);
        regBtn = findViewById(R.id.reg_btn);

        editLogin = findViewById(R.id.edit_login);
        editPassword = findViewById(R.id.edit_password);

        exSaveBox = findViewById(R.id.ex_save_box);

        exSaveBox.setChecked(exSave);

        exSaveBox.setOnCheckedChangeListener((v, ch)-> {
            exSave = ch;
            storage.saveSharedBool(EX_SAVE, ch);
        });

        okBtn.setOnClickListener(v-> {
            if(storage.isExist(LOG_PASS, exSave)) {
                String inputLogin = editLogin.getText().toString();
                String inputPassword = editPassword.getText().toString();
                String inputLogPass = inputLogin + "\n" + inputPassword + "\n";
                String logPass = storage.readFile(LOG_PASS, exSave);
                if(inputLogPass.equals(logPass)) {
                    message = getResources().getText(R.string.valid_msg).toString();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    message = getResources().getText(R.string.invalid_msg).toString();
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            } else {
                message = getResources().getText(R.string.need_register).toString();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        regBtn.setOnClickListener(v-> {
            String inputLogin = editLogin.getText().toString();
            String inputPassword = editPassword.getText().toString();
            if(!"".equals(inputLogin) && !"".equals(inputPassword)) {
                String inputLogPass = inputLogin + "\n" + inputPassword + "\n";
                storage.saveFile(LOG_PASS, inputLogPass, exSave);
                message = getResources().getText(R.string.reg_ok).toString();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            } else {
                message = getResources().getText(R.string.fill_fields).toString();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });



    }


}
