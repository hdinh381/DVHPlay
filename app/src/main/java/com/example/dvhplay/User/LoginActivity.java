package com.example.dvhplay.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.dvhplay.MainActivity;
import com.example.dvhplay.Models.User;
import com.example.dvhplay.R;
import com.example.dvhplay.databinding.ActivityLoginBinding;
import com.example.dvhplay.helper.SQLHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    final String MD5 = "MD5";
    ActivityLoginBinding binding;
    LoginViewListener listener = new LoginViewListener();
    SQLHelper sqlHelper;
    PasswordUtil passwordUtil;
    List<User> users = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        passwordUtil = new PasswordUtil();
        sqlHelper = new SQLHelper(getBaseContext());
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.user.setError(null);
                binding.user.setErrorEnabled(false);
                binding.pass.setError(null);
                binding.pass.setErrorEnabled(false);
                listener.onLogin(binding.user,binding.pass);
            }
        });
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.user.setError(null);
                binding.user.setErrorEnabled(false);
                binding.pass.setError(null);
                binding.pass.setErrorEnabled(false);
                listener.onRegister(binding.user,binding.pass);
            }
        });
        binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),getText(R.string.featureDevelopment),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class LoginViewListener {
        void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
            if (sqlHelper.getAllUser().size()!=0) users = sqlHelper.getAllUser();
            else {
                loginUser.setError(getText(R.string.usernameNotExist));
                Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                return;
            }
            if (isValidEmail(loginUser.getEditText().getText())){
                if (!loginUser.getEditText().getText().toString().isEmpty()){
                    if (users.size()!=0) {
                        for (int i =0; i<users.size();i++) {
                            if (loginUser.getEditText().getText().toString().equals(users.get(i).getUsername())) {
                                if (md5(loginPass.getEditText().getText().toString()).equals(users.get(i).getPassword())) {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    Toast.makeText(getBaseContext(),getText(R.string.loginSuccess)+" "+getText(R.string.wellcome)+" "+users.get(i).getUsername(),Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                } else {
                                    loginPass.setError(getText(R.string.incorrectPassword));
                                    Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            } else {
                                loginUser.setError(getText(R.string.usernameNotExist));
                                Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    } else {
                        loginUser.setError(getText(R.string.usernameNotExist));
                        Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    loginUser.setError(getText(R.string.username_empty));
                    Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                loginUser.setError(getText(R.string.invalid_email));
                Toast.makeText(getBaseContext(),getText(R.string.login_failed),Toast.LENGTH_SHORT).show();
                return;
            }
        }
        void onRegister(TextInputLayout loginUser, TextInputLayout loginPass) {
            if (sqlHelper.getAllUser().size()!=0) users = sqlHelper.getAllUser();
            if (loginUser.getEditText().getText().toString().isEmpty()) {
                loginUser.setError(getText(R.string.username_empty));
                return;
            }
            if (!isValidEmail(loginUser.getEditText().getText())) {
                loginUser.setError(getText(R.string.invalid_email));
                return;
            }
            if (users.size()!=0) {
                for (int i =0; i<users.size();i++) {
                    if (users.get(i).getUsername().equals(loginUser.getEditText().getText().toString())) {
                        loginUser.setError(getText(R.string.usernameAvailable));
                        return;
                    }
                }
            }
            if (!passwordUtil.hasLength(loginPass.getEditText().getText().toString())) {
                loginPass.setError(getText(R.string.invalid_password));
                return;
            }
            if (!passwordUtil.hasLowerCase(loginPass.getEditText().getText().toString())) {
                loginPass.setError(getText(R.string.hasLower));
                return;
            }if (!passwordUtil.hasUpperCase(loginPass.getEditText().getText().toString())) {
                loginPass.setError(getText(R.string.hasUpper));
                return;
            }if (!passwordUtil.hasSymbol(loginPass.getEditText().getText().toString())) {
                loginPass.setError(getText(R.string.hasSymbol));
                return;
            }if (!passwordUtil.hasSpace(loginPass.getEditText().getText().toString())) {
                loginPass.setError(getText(R.string.hasSpaces));
                return;
            } sqlHelper.insertUser(loginUser.getEditText().getText().toString().trim(),md5(loginPass.getEditText().getText().toString()));
            Toast.makeText(getBaseContext(),getText(R.string.registerSuccess),Toast.LENGTH_SHORT).show();
        }
    }
    public String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}