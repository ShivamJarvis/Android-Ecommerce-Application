package com.codebrew.techdeal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchTypeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SearchView searchEditText;

    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_type);
        searchEditText = findViewById(R.id.main_search_box);



        listView = findViewById(R.id.search_list_view);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setOnItemClickListener(this);
        updateRecentSearches();


        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("RecentSearches");
                parseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
                parseQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects!=null){
                            if(e==null){
                                if (objects.size()<4){
                                    ParseObject parseObject = new ParseObject("RecentSearches");
                                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                                    parseObject.put("text",searchEditText.getQuery().toString());
                                    parseObject.saveInBackground();
                                }
                                else{
                                    for(int position=3; position>0; position--){
                                        objects.get(position).put("text",objects.get(position-1).getString("text"));
                                        objects.get(position).put("username",objects.get(position-1).getString("username"));
                                        objects.get(position).saveInBackground();
                                    }
                                    objects.get(0).put("text",searchEditText.getQuery().toString());
                                    objects.get(0).saveInBackground();
                                    arrayAdapter.notifyDataSetChanged();
                                }
                            }

                        }

                    }
                });

                Intent intent = new Intent(SearchTypeActivity.this,SearchFoundActivity.class);
                intent.putExtra("searchKey",searchEditText.getQuery().toString());
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {

    }
    private void updateRecentSearches(){
        ParseQuery<ParseObject> recentParseQuery = ParseQuery.getQuery("RecentSearches");
        recentParseQuery.whereEqualTo("username",ParseUser.getCurrentUser().getUsername());
        recentParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null){
                    if(e==null && objects.size()>0){
                        for(ParseObject object:objects){
                            arrayList.add(object.getString("text"));
                        }
                        listView.setAdapter(arrayAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchTypeActivity.this,SearchFoundActivity.class);
        intent.putExtra("searchKey",arrayList.get(position));
        startActivity(intent);
    }
}