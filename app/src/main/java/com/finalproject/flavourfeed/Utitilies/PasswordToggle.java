package com.finalproject.flavourfeed.Utitilies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.finalproject.flavourfeed.R;
import com.google.android.material.textfield.TextInputLayout;

public class PasswordToggle {
    public static void changeToggleIcon(Context context, TextInputLayout passwordTextInputLayout, EditText passwordEditText) {

        final Drawable showPasswordIcon = ContextCompat.getDrawable(context, R.drawable.newshowpasswordicon);
        final Drawable hidePasswordIcon = ContextCompat.getDrawable(context, R.drawable.newhidepasswordicon);
        final boolean[] passwordVisible = {false};

        passwordTextInputLayout.setEndIconDrawable(hidePasswordIcon);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (passwordVisible[0]) {
                    passwordTextInputLayout.setEndIconDrawable(showPasswordIcon);
                } else {
                    passwordTextInputLayout.setEndIconDrawable(hidePasswordIcon);
                }
            }
        });

        passwordTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordVisible[0] = !passwordVisible[0];
                if (passwordVisible[0]) {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordTextInputLayout.setEndIconDrawable(showPasswordIcon);
                } else {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordTextInputLayout.setEndIconDrawable(hidePasswordIcon);
                }
            }
        });
    }
}
