package org.projects.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.TextView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v4.view.MenuItemCompat;


/*import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;*/
/*
import com.google.android.gms.common.api.GoogleApiClient;
*/

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
    public void saveCopy()
    {
        lastDeletedPosition = listView.getCheckedItemPosition();
        lastDeletedProduct =  (Product) listView.getItemAtPosition(lastDeletedPosition);
    }
    @Override
    public void onPositiveClicked() {
        //Do your update stuff here to the listview
        //and the bag etc
        //just to show how to get arguments from the bag.
        Toast toast = Toast.makeText(context,
                "You cleared your list!", Toast.LENGTH_LONG);
        toast.show();
         int listCount = adapter.getCount();
                for(int i=listCount-1;i>=0;i--){
                    getMyAdapter().getRef(i).setValue(null);
                }

                getMyAdapter().notifyDataSetChanged();
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
                if (remove >= 0 ) {
                    saveCopy();
                    int index = listView.getCheckedItemPosition();
                    getMyAdapter().getRef(index).setValue(null);
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
                qValue = Integer.parseInt(qText.getText().toString());
/*
                bag.add(new Product(value, qValue));
*/
                Product p = new Product(value, qValue);
                firebase.push().setValue(p);

                //The next line is needed in order to say to the ListView
                //that the data has changed - we have added stuff now!
                getMyAdapter().notifyDataSetChanged();
            }
        });
        //The next line is needed in order to say to the ListView
        //that the data has changed - we have added stuff now!

        //add some stuff to the list so we have something
        // to show on app startup

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Locate MenuItem with ShareActionProvider
        // Fetch and store ShareActionProvider
        // Return true to display menu
        /*mShareActionProvider.setShareIntent(getShareIntent());*/
        return true;
    }






    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.item_delete:
                int listCount = adapter.getCount();
                for(int i=listCount-1;i>=0;i--){
                    getMyAdapter().getRef(i).setValue(null);
                }

                getMyAdapter().notifyDataSetChanged();
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //Convert the listview to strings and collect them in a Strinbuilder
        ArrayList<String> shareList = new ArrayList<String>();
        StringBuilder listString = new StringBuilder();
        shareList.add("Min inkøbsliste: ");
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
            intent.setType("text/plain"); //MIME type
            String textToShare = listString.toString();
            intent.putExtra(Intent.EXTRA_TEXT, textToShare); //add the text to t
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   /* public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*        client.connect();*/
/*
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
/*
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
*/
/*        client.disconnect();*/
    }
}

