package org.test.karenheadley.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
9/27/16 CodePath Android Mobile Bootcamp

        Pre-Work To Do List Application
 */

public class MainActivity extends AppCompatActivity {
ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    private final int REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get a handle to ListView
        lvItems = (ListView)findViewById(R.id.IvItems);
        //create an arraylist - replaced by load items from text file
        //items = new ArrayList<>();
        readItems();  // load items from text file
        //create an arrayadapter
        itemsAdapter = new ArrayAdapter<> (this, android.R.layout.simple_list_item_1, items);
        // attach adapter to ListView - adapter displays Arraylist contents within a ListView:
        lvItems.setAdapter(itemsAdapter);
        //items.add("First Item");
        //items.add("Second Item");

        setupListViewListener();

        setupOnClickListener();
    }

    // Long click will delete item - remove that item and refresh the adapter
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }
        );
    }

    /*
    Single click will bring up Edit form with passed item body
    Edit form activity uses an intent
    Text and position of item to be edited are passed to the edit form
    */

    private void setupOnClickListener(){
                lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
                                i.putExtra("item",items.get(position));
                                i.putExtra("position",position);
                                startActivityForResult(i, REQUEST_CODE);
                            }
                    });
            }

    // Add new items to the list & and persist them to text file
    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    // Updated to do item from edit form is inserted into array and adapter is notified of the change
    // Write items back to the persisted text file
    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_CODE && resultCode == RESULT_OK ) {
                            String myStr=data.getStringExtra("edittextvalue");
                            int pos=data.getIntExtra("position",0);
                            items.set(pos,myStr);
                            itemsAdapter.notifyDataSetChanged();
                            writeItems();
                        }
                }

    // Opens a file and reads a newline-delimited list of items
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            // Apache Commons IO library dependency needed for FileUtils function
            items=new ArrayList<String>(FileUtils.readLines(todoFile));

        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    // Saves a file and writes a newline-delimited list of items
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try{
            // Apache Commons IO library dependency needed for FileUtils function
            FileUtils.writeLines(todoFile, items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}