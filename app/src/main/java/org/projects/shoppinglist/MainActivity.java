package org.projects.shoppinglist;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements deleteFragment.OnPositiveListener {
    Product lastDeletedProduct;
    int lastDeletedPosition;
    static Context context;
    Button mButton;
    EditText mEdit;
    EditText qText;
    int qValue;
    String value;
    Button dButton;
    Button cButton;
    static FirebaseListAdapter<Product> adapter;
    ListView listView;
    private ShareActionProvider mShareActionProvider;
    ArrayList<Product> bag = new ArrayList<Product>();
    public static FirebaseListAdapter getMyAdapter(){return adapter;}
    static deleteFragment dialog;
    public View viewW;
    public void saveCopy()
    {
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct =  (Product) listView.getItemAtPosition(lastDeletedPosition);
    }
    @Override
    public void onPositiveClicked() {
        int listCount = adapter.getCount();
        if(listCount >0){
            Toast toast = Toast.makeText(context,
                    "You cleared your list!", Toast.LENGTH_LONG);
            toast.show();
            for(int i=listCount-1;i>=0;i--){
                getMyAdapter().getRef(i).setValue(null);
            }

            getMyAdapter().notifyDataSetChanged();
        }else {
            Toast toast = Toast.makeText(context,
                    "Nothing to clear", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    public static class MyDialog extends deleteFragment {


        @Override
        protected void negativeClick() {
            //Here we override the method and can now do something
            Toast toast = Toast.makeText(context,
                    "Your list have been saved from clearing!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    public void showDialog(View v) {
        //showing our dialog.

        dialog = new MyDialog();
        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "deleteFragment");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.addButton);
        dButton = (Button) findViewById(R.id.deleteSelctedButton);
        cButton = (Button) findViewById(R.id.clearListButton);
        listView = (ListView) findViewById(R.id.list);
        final View parent = findViewById(R.id.layout);

        this.context = this;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("saveBag"))
                bag = savedInstanceState.getParcelableArrayList("saveBag");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DatabaseReference firebase = FirebaseDatabase.getInstance().getReference().child("items");
        adapter = new FirebaseListAdapter<Product>(this, Product.class, android.R.layout.simple_list_item_checked, firebase) {
            @Override
            protected void populateView(View view, Product product, int i) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1); //standard android id.
                textView.setText(product.toString());
            }
        };
        listView.setAdapter(adapter);

        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int remove = listView.getCheckedItemPosition();
                if (remove >= 0) {
                    saveCopy();
                    listView.clearChoices();
                    getMyAdapter().getRef(remove).setValue(null);
                    getMyAdapter().notifyDataSetChanged();
                }
                final View parent = listView;
                Snackbar snackbar = Snackbar
                        .make(parent, "Item Deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Product p = new Product(lastDeletedProduct.name, lastDeletedProduct.quantity);
                                firebase.push().setValue(p);
                                getMyAdapter().notifyDataSetChanged();
                                Snackbar snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        });

                snackbar.show();
            }

        });

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v);

            }
        });
        //here we set the choice mode - meaning in this case we can
        //only select one item at a time.
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get text from userinput
                mEdit = (EditText) findViewById(R.id.editText);
                value = mEdit.getText().toString();
                qText = (EditText) findViewById(R.id.quantityText);

                if(!qText.getText().toString().equals("")) {
                    qValue = Integer.parseInt(qText.getText().toString());
                    Product p = new Product(value, qValue);
                    firebase.push().setValue(p);

                    //The next line is needed in order to say to the ListView
                    //that the data has changed - we have added stuff now!
                    getMyAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.item_delete:
                /*int listCount = adapter.getCount();
                for(int i=listCount-1;i>=0;i--){
                    getMyAdapter().getRef(i).setValue(null);
                }

                getMyAdapter().notifyDataSetChanged();*/
                showDialog(viewW);

        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //Convert the listview to strings and collect them in a Strinbuilder
        ArrayList<String> shareList = new ArrayList<String>();
        StringBuilder listString = new StringBuilder();
        shareList.add("Min indkøbsliste: ");
        for(int i=0; i<listView.getCount();i++){
            Product p = (Product) listView.getItemAtPosition(i);
            String s = p.toString();
            shareList.add(s+", ");
        }
        for(String s: shareList){
            listString.append(s);
        }
        if(id == R.id.menu_item_share && listView.getCount() !=0){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String textToShare = listString.toString();
            intent.putExtra(Intent.EXTRA_TEXT, textToShare);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ALWAYS CALL THE SUPER METHOD - To be nice!
        super.onSaveInstanceState(outState);
        /* Here we put code now to save the state */
        outState.putParcelableArrayList("saveBag", bag);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

