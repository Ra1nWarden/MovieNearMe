package com.project.movienearme.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieManager;

/**
 * A dialog for adding and editing section for a movie.
 */
public final class EditSectionFragment extends DialogFragment {

    public static final String TAG = "EditSectionFragment";
    private static final String POSITION_KEY = "position";
    private static final String CINEMA_ID_KEY = "cinemaId";
    private static final String SHOW_TIME_KEY = "showTime";
    private static final int NONE = -1;
    private OnSectionDetailsChangedListener listener;

    public static EditSectionFragment getInstance(int position, int cinemaId, int showTime) {
        EditSectionFragment f = new EditSectionFragment();
        Bundle args = new Bundle();
        args.putInt(POSITION_KEY, position);
        args.putInt(CINEMA_ID_KEY, cinemaId);
        args.putInt(SHOW_TIME_KEY, showTime);
        f.setArguments(args);
        return f;
    }

    public void setOnSectionDetailsChangedListener(OnSectionDetailsChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View form = inflater.inflate(R.layout.add_section, null);
        MovieManager manager = new MovieManager(getActivity());
        final int position = getArguments() == null ? NONE : getArguments().getInt(POSITION_KEY,
                NONE);
        int editTime = getArguments() == null ? NONE : getArguments().getInt(SHOW_TIME_KEY, NONE);
        int editCinemaId = getArguments() == null ? NONE : getArguments().getInt(CINEMA_ID_KEY,
                NONE);
        final Spinner cinemaSpinner = (Spinner) form.findViewById(R.id.cinema_spinner);
        final SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout
                .simple_list_item_1, manager.getAllCinema(), new String[]{"cinema_name"}, new
                int[]{android.R.id.text1}, 0);
        cinemaSpinner.setAdapter(adapter);
        if (editCinemaId != NONE) {
            for (int i = 0; i < cinemaSpinner.getCount(); i++) {
                long id = cinemaSpinner.getItemIdAtPosition(i);
                if (id == editCinemaId) {
                    cinemaSpinner.setSelection(i);
                }
            }
        }
        final TimePicker picker = (TimePicker) form.findViewById(R.id.time_picker);
        picker.setIs24HourView(true);
        if (editTime != NONE) {
            picker.setCurrentHour(editTime / 60);
            picker.setCurrentMinute(editTime % 60);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.edit_section)
                .setView(form)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            int time = picker.getCurrentHour() * 60 + picker.getCurrentMinute();
                            long id = cinemaSpinner.getSelectedItemId();
                            listener.onChangeConfirmed(position, (int) id, time);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        Dialog dialog = builder.create();
        return dialog;
    }

    public interface OnSectionDetailsChangedListener {
        void onChangeConfirmed(int position, int cinemaId, int showTime);
    }
}
