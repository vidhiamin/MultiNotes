package com.example.secondassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    int index = 0;
    private EditText addEditTitle;
    private EditText addEditDetails;
    String prevTitle = "";
    String prevDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        addEditTitle = (EditText) findViewById(R.id.addEditTitle);
        addEditDetails = (EditText) findViewById(R.id.addEditDetails);
        addEditDetails.setMovementMethod(new ScrollingMovementMethod());

        Note n = (Note) getIntent().getSerializableExtra("MainActivity");
        if(n!=null) {
            addEditTitle.setText(n.getTitle());
            addEditDetails.setText(n.getDesc());
            index = n.getIndex();
            prevTitle = n.getTitle();
            prevDesc = n.getDesc();
        }
    }

    public boolean isDataModified(){
        if(prevTitle.equals(addEditTitle.getText().toString()) && prevDesc.equals(addEditDetails.getText().toString()))  {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.saveIcon:
                if(addEditTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.untitled_note_error), Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isDataModified()){
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        Note newNote = new Note(addEditTitle.getText().toString(),
                                addEditDetails.getText().toString(),"",index);
                        bundle.putSerializable("EditActivity", newNote);
                        setResult(RESULT_OK,data);
                        data.putExtras(bundle);
                    }
                }
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(addEditTitle.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.untitled_note_error), Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            if (isDataModified()) {
                String dialogBody = "Your note is not saved! Save note '"+addEditTitle.getText().toString()+"'?";
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage(dialogBody);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent data = new Intent();
                        Bundle bundle = new Bundle();
                        Note newNote = new Note(addEditTitle.getText().toString(),
                                addEditDetails.getText().toString(),"",index);
                        bundle.putSerializable("EditActivity", newNote);
                        setResult(RESULT_OK,data);
                        data.putExtras(bundle);
                        finish();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alert.show();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("TITLE", addEditTitle.getText().toString());
        outState.putString("DESC", addEditDetails.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        addEditTitle.setText(savedInstanceState.getString("TITLE"));
        addEditDetails.setText(savedInstanceState.getString("DESC"));
    }



}
