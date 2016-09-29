package org.test.karenheadley.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    String item;
    int position;
    EditText etEditNew;
    EditText etEdited ;

    @Override
    // Receives item body from mainactivity
    // Leave cursor at the end of current text value & and set focus there
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        item = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position",0);
        etEdited = (EditText) findViewById(R.id.etEdit);
        etEdited.requestFocus();
        etEdited.setText(item);
        etEdited.setSelection(etEdited.getText().length());

    }

    // Send back the todo item data to the list activity
    public void onSave(View view) {
        Intent intent = new Intent();
        etEditNew = (EditText) findViewById(R.id.etEdit);
        intent.putExtra("edittextvalue",etEditNew.getText().toString());
        intent.putExtra("position",position);
        setResult(RESULT_OK, intent);
        finish();
    }
}

