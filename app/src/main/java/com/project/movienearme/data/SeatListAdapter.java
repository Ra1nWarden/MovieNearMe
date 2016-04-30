package com.project.movienearme.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.project.movienearme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A list adapter for displaying available seats.
 */
public final class SeatListAdapter extends BaseAdapter {

    private final Context context;
    private List<Seat> seats;

    public SeatListAdapter(Context context, int total, int listingId) {
        this.context = context;
        MovieManager manager = new MovieManager(context);
        seats = new ArrayList<>();
        for (int i = 1; i <= total; i++) {
            Seat seat = new Seat();
            seat.enabled = manager.isSeatAvailableForId(listingId, i);
            seat.available = seat.enabled;
            seat.seatId = i;
            seats.add(seat);
        }
    }

    public void toggle(int position) {
        Seat seat = seats.get(position);
        seat.available = !seat.available;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return seats.size();
    }

    @Override
    public Object getItem(int position) {
        return seats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.selection_list_item, parent, false);
            final SeatListAdapter.ViewHolder viewHolder = new SeatListAdapter.ViewHolder();
            viewHolder.idView = (TextView) v.findViewById(R.id.item_title);
            viewHolder.checkBox = (CheckBox) v.findViewById(R.id.selection_box);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton
                    .OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Seat seat = (Seat) viewHolder.checkBox.getTag();
                    seat.available = !isChecked;
                }
            });
            v.setTag(viewHolder);
            viewHolder.checkBox.setTag(getItem(position));
        } else {
            ((ViewHolder) v.getTag()).checkBox.setTag(getItem(position));
        }
        Seat seat = (Seat) getItem(position);
        SeatListAdapter.ViewHolder viewHolder = (SeatListAdapter.ViewHolder) v.getTag();
        viewHolder.idView.setText(String.format("%d排%d号", seat.seatId / 10 + 1, seat.seatId % 10 + 1));
        viewHolder.checkBox.setChecked(!seat.available);
        viewHolder.checkBox.setEnabled(seat.enabled);
        return v;
    }

    public List<Integer> getSelectedSeats() {
        List<Integer> selected = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.enabled && !seat.available) {
                selected.add(seat.seatId);
            }
        }
        return selected;
    }

    public static class ViewHolder {
        public TextView idView;
        public CheckBox checkBox;
    }

    public class Seat {
        public int seatId;
        public boolean available;
        public boolean enabled;
    }
}
