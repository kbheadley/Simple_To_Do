package org.test.karenheadley.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by kheadley on 9/29/2016.
 */

public class TodoCustomAdapter extends ArrayAdapter<TodoItem> {
    public TodoCustomAdapter(Context context, ArrayList<TodoItem> todoItems) {
        super(context, 0, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItem todoItem = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }
        TextView tvText = (TextView) convertView.findViewById(R.id.itemText);
        tvText.setText(todoItem.text);
        return convertView;
    }
}