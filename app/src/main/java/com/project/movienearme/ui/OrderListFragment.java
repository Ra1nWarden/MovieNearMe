package com.project.movienearme.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.data.OrderListAdapter;
import com.project.movienearme.data.OrderManager;
import com.project.movienearme.data.UserManager;

import java.util.List;

/**
 * A fragment for listing orders.
 */
public final class OrderListFragment extends ListFragment {

    @Override
    public void onListItemClick(ListView l, View view, int position, long id) {
        super.onListItemClick(l, view, position, id);
        OrderListAdapter.ViewHolder viewHolder = (OrderListAdapter.ViewHolder) view.getTag();
        final MovieManager manager = new MovieManager(getActivity());
        final OrderManager orderManager = new OrderManager(getActivity());
        final int listingId = viewHolder.listingId;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout forms = (LinearLayout) inflater.inflate(R.layout.order_detail, null);
        TextView movieName = (TextView) forms.findViewById(R.id.movie_name_value);
        movieName.setText(manager.getMovieTitleForId(manager.getMovieIdForListing(listingId)));
        TextView cinemaName = (TextView) forms.findViewById(R.id.cinema_name_value);
        cinemaName.setText(manager.getCinemaName(manager.getCinemaIdForListing(listingId)));
        TextView cinemaAddress = (TextView) forms.findViewById(R.id.cinema_address_value);
        cinemaAddress.setText(manager.getCinemaAddress(manager.getCinemaIdForListing(listingId)));
        TextView timeField = (TextView) forms.findViewById(R.id.show_time_value);
        timeField.setText(String.format("%02d:%02d", viewHolder.showTime / 60, viewHolder
                .showTime % 60));
        TextView seatField = (TextView) forms.findViewById(R.id.seat_value);
        final UserManager userManager = new UserManager(getActivity());
        List<String> seats = manager.getSeatsForUsernameAndListingId(userManager
                .getLoggedInUsername(), listingId);
        seatField.setText(TextUtils.join(",", seats));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.order_details)
                .setView(forms)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_order, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (orderManager.removeOrder(listingId, userManager.getLoggedInUsername()
                        )) {
                            toast(getActivity(), R.string.cancel_success);
                        } else {
                            toast(getActivity(), R.string.cancel_failure);
                        }
                        OrderListAdapter listAdapter = new OrderListAdapter(getActivity(),
                                manager.getOrdersForUsername
                                (userManager.getLoggedInUsername()));
                        OrderListFragment.this.setListAdapter(listAdapter);
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TEST", "onResume()");
    }

    private void toast(Context context, int failedTextId) {
        String failedText = context.getResources().getString(failedTextId);
        Toast.makeText(context, failedText, Toast.LENGTH_SHORT).show();
    }
}
