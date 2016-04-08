package com.project.movienearme.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.movienearme.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * List adapter for displaying sections for a movie.
 */
public final class SectionListAdapter extends BaseAdapter {

    private final Context context;
    private MovieManager manager;
    private List<Section> sections;

    public SectionListAdapter(Context context) {
        this.context = context;
        this.manager = new MovieManager(context);
        this.sections = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            SectionListAdapter.ViewHolder viewHolder = new SectionListAdapter.ViewHolder();
            viewHolder.cinemaText = (TextView) v.findViewById(R.id.item_title);
            viewHolder.timeText = (TextView) v.findViewById(R.id.item_value);
            v.setTag(viewHolder);
        }
        SectionListAdapter.ViewHolder viewHolder = (SectionListAdapter.ViewHolder) v.getTag();
        Section item = (Section) getItem(position);
        viewHolder.cinemaText.setText(manager.getCinemaName(item.cinemaId));
        viewHolder.timeText.setText(String.format("%02d:%02d", item.showTime / 60, item.showTime
                % 60));
        return v;
    }

    public void edit(int position, int id, int time) {
        if (position == -1) {
            Section s = new Section();
            s.cinemaId = id;
            s.showTime = time;
            sections.add(s);
        } else {
            Section s = (Section) getItem(position);
            s.cinemaId = id;
            s.showTime = time;
        }
        notifyDataSetChanged();
    }

    public List<Section> getAllSections() {
        return sections;
    }

    public class Section {
        public int cinemaId;
        public int showTime;
    }

    static class ViewHolder {
        public TextView cinemaText;
        public TextView timeText;
    }

}
