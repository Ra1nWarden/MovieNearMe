package com.project.movienearme.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.movienearme.R;
import com.project.movienearme.data.MovieManager;
import com.project.movienearme.data.SectionListAdapter;
import com.project.movienearme.ui.EditSectionFragment;

import java.util.List;

/**
 * An activity for adding movies.
 */
public final class AddMovieActivity extends AppCompatActivity implements EditSectionFragment
        .OnSectionDetailsChangedListener {

    private ListView sectionList;
    private SectionListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.add_movie);
        setUpActionBar();
        sectionList = (ListView) findViewById(R.id.section_list);
        adapter = new SectionListAdapter(this);
        sectionList.setAdapter(adapter);
        sectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SectionListAdapter.Section item = (SectionListAdapter.Section) adapter.getItem
                        (position);
                EditSectionFragment editSection = EditSectionFragment.getInstance(position, item
                        .cinemaId, item.showTime);
                editSection.setOnSectionDetailsChangedListener(AddMovieActivity.this);
                editSection.show(getSupportFragmentManager(), EditSectionFragment.TAG);
            }
        });
        TextView addSectionLink = (TextView) findViewById(R.id.add_section_link);
        addSectionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditSectionFragment editSection = new EditSectionFragment();
                editSection.setOnSectionDetailsChangedListener(AddMovieActivity.this);
                editSection.show(getSupportFragmentManager(), EditSectionFragment.TAG);
            }
        });
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        LayoutInflater inflater = LayoutInflater.from(this);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        View actionBarView = inflater.inflate(R.layout.action_bar, null);
        TextView titleView = (TextView) actionBarView.findViewById(R.id.title_text);
        titleView.setText(R.string.add_movie);
        ImageButton leftButton = (ImageButton) actionBarView.findViewById(R.id.left_button);
        leftButton.setImageResource(R.drawable.ic_clear);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMovieActivity.this)
                        .setTitle(R.string.exit_editing)
                        .setMessage(R.string.confirm_exit_editing)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ImageButton rightButton = (ImageButton) actionBarView.findViewById(R.id.right_button);
        rightButton.setImageResource(R.drawable.ic_done);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = getInputFromTextField(R.id.movie_name_field);
                String movieCast = getInputFromTextField(R.id.movie_cast_field);
                String movieDescription = getInputFromTextField(R.id.movie_description_field);
                if (movieName.isEmpty() || movieCast.isEmpty() || movieDescription.isEmpty()) {
                    Toast.makeText(AddMovieActivity.this, R.string.empty_input_error, Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                List<SectionListAdapter.Section> inputSections = adapter.getAllSections();
                if (inputSections.isEmpty()) {
                    Toast.makeText(AddMovieActivity.this, R.string.empty_section_error, Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                MovieManager manager = new MovieManager(AddMovieActivity.this);
                boolean success = manager.addMovieWithSections(movieName, movieDescription,
                        movieCast, inputSections);
                Toast.makeText(AddMovieActivity.this, success ? R.string.add_success : R.string
                        .add_failure, Toast.LENGTH_SHORT).show();
                AddMovieActivity.this.finish();
            }
        });
        actionBar.setCustomView(actionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    public String getInputFromTextField(int id) {
        EditText view = (EditText) findViewById(id);
        return view.getText().toString();
    }

    @Override
    public void onChangeConfirmed(int position, int cinemaId, int showTime) {
        adapter.edit(position, cinemaId, showTime);
    }
}
