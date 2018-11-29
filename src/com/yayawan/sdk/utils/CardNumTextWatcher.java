package com.yayawan.sdk.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class CardNumTextWatcher implements TextWatcher {

    private EditText numberEditText;

    int beforeLen = 0;

    int afterLen = 0;

    public CardNumTextWatcher(EditText numberEditText) {
        this.numberEditText = numberEditText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        beforeLen = s.length();
    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = numberEditText.getText().toString();
        afterLen = txt.length();

        if (afterLen > beforeLen) {
            if (txt.length() == 5 || txt.length() == 10 || txt.length() == 15
                    || txt.length() == 20 || txt.length() == 25 || txt.length() == 30) {
                numberEditText.setText(new StringBuffer(txt).insert(
                        txt.length() - 1, " ").toString());
                numberEditText.setSelection(numberEditText.getText().length());
            }
        } else {
            if (txt.startsWith(" ")) {
                numberEditText.setText(new StringBuffer(txt).delete(
                        afterLen - 1, afterLen).toString());
                numberEditText.setSelection(numberEditText.getText().length());

            }
        }

    }

}
