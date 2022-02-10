package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListViewAdapter extends BaseAdapter {
        private LayoutInflater li;
        private Context context;

        public CustomListViewAdapter(Context context, ArrayList<Record> listData) {
            li = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return IOManager.data.size();
        }

        @Override
        public Record getItem(int position) {
            return IOManager.data.get(position);
        }

        @Override public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView != null) {
                holder = (ViewHolder) convertView.getTag();
            }else{
                convertView = li.inflate(R.layout.row_layout, null);
                holder = new ViewHolder();

                holder.name = (TextView) convertView.findViewById(R.id.nameTextView);
                holder.score = (TextView) convertView.findViewById(R.id.scoreTextView);
                holder.datetime = (TextView) convertView.findViewById(R.id.dateTextView);

                convertView.setTag(holder);
            }

            holder.name.setText(IOManager.data.get(position).getName());
            holder.score.setText(String.valueOf(IOManager.data.get(position).getScore()));
            holder.datetime.setText(IOManager.data.get(position).getDatetime());

            return convertView;
        }

         static class ViewHolder {
            TextView name;
            TextView score;
            TextView datetime;
        }
    }

