package com.project.movienearme.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.movienearme.R;

/**
 * An adapter for displaying user information.
 */
public final class ProfileListAdapter extends BaseAdapter {

    private String[] items;
    private final Context context;
    private UserManager userManager;

    public ProfileListAdapter(Context context) {
        this.context = context;
        items = context.getResources().getStringArray(R.array.personal_profile_items);
        userManager = new UserManager(context);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.list_item, parent, false);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.titleView = (TextView) view.findViewById(R.id.item_title);
            viewHolder.valueView = (TextView) view.findViewById(R.id.item_value);
            view.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.titleView.setText(items[position]);
        switch (position) {
            case 0:
                holder.valueView.setText(userManager.getLoggedInUserField("username"));
                break;
            case 1:
                holder.valueView.setText(userManager.getLoggedInUserField("real_name"));
                break;
            case 2:
                holder.valueView.setText(userManager.getLoggedInUserField("phone_number"));
                break;
        }
        return view;
    }

    static class ViewHolder {
        TextView titleView;
        TextView valueView;
    }
}

