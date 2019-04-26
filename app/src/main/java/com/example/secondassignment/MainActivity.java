package com.example.secondassignment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private List<Note> notesLst = new ArrayList<>();
    private static final int EDIT_NOTE = 1, NEW_NOTE = 2, FIRST_INDEX = 0;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        notesAdapter = new NotesAdapter(notesLst,this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new MyAsyncTask().execute();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.baseline_help_white_48);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutIcon:
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
                return true;
            case R.id.addNoteIcon:
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, NEW_NOTE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Note n = notesLst.get(pos);
        n.setIndex(pos);
        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("MainActivity", n);
        startActivityForResult(intent, EDIT_NOTE);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        Note n = notesLst.get(pos);
        String dialogBody = "Delete Note '"+n.getTitle()+"'?";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(dialogBody);
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                notesLst.remove(pos);
                changeTitleBar();
                notesAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_NOTE) {
            if (resultCode == RESULT_OK) {
                if (!notesLst.isEmpty()) {
                    Note n = (Note) data.getSerializableExtra("EditActivity");
                    notesLst.remove(n.getIndex());
                    n.setDate(getCurrDateTime());
                    notesLst.add(FIRST_INDEX, n);
                    changeTitleBar();
                    notesAdapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == NEW_NOTE) {
            if (resultCode == RESULT_OK) {
                Note n = (Note) data.getSerializableExtra("EditActivity");
                n.setDate(getCurrDateTime());
                notesLst.add(FIRST_INDEX, n);
                changeTitleBar();
                notesAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getCurrDateTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, hh:mm aaa");
        String strDate = sdf.format(calendar.getTime());
        return strDate;
    }

    void changeTitleBar(){
        getSupportActionBar().setTitle(getString(R.string.app_name) + "(" + notesLst.size() + ")");
    }

    @Override
    protected void onPause() {
        super.onPause();
        writeJsonFile();
    }

    public void writeJsonFile() {
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.filename), Context.MODE_PRIVATE);
            JsonWriter jWriter = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            jWriter.setIndent(" ");
            jWriter.beginArray();
            for (Note n : notesLst) {
                jWriter.beginObject();
                jWriter.name("noteTitle").value(n.getTitle());
                jWriter.name("noteDesc").value(n.getDesc());
                jWriter.name("noteDate").value(n.getDate());
                jWriter.endObject();
            }
            jWriter.endArray();
            jWriter.close();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }
    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            changeTitleBar();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                readJsonFile();
            } catch (Exception e) {
            }
            return null;
        }
    }


    public void readJsonFile() {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.filename));
            JsonReader jReader = new JsonReader(new InputStreamReader(inputStream, getString(R.string.encoding)));
            jReader.beginArray();

            while (jReader.hasNext()) {
                Note n = new Note();
                jReader.beginObject();
                while (jReader.hasNext()) {
                    String name = jReader.nextName();
                    if (name.equals("noteTitle")) {
                        n.setTitle(jReader.nextString());
                    } else if (name.equals("noteDesc")) {
                        n.setDesc(jReader.nextString());
                    } else if (name.equals("noteDate")) {
                        n.setDate(jReader.nextString());
                    } else
                        jReader.skipValue();
                }
                jReader.endObject();
                notesLst.add(n);
            }
            jReader.endArray();
        } catch (FileNotFoundException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
