package org.projects.shoppinglist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements deleteFragment.OnPositiveListener{

    static deleteFragment dialog;
    static Context context;
    Button mButton;
    EditText mEdit;
    EditText qText;
    int qValue;
    String value;
    Button dButton;
    Button cButton;
    ArrayAdapter<Product> adapter;
    ListView listView;
    ArrayList<Product> bag = new ArrayList<Product>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public ArrayAdapter getMyAdapter() {
        return adapter;
    }
    @Override
    public void onPositiveClicked() {
        //Do your update stuff here to the listview
        //and the bag etc
        //just to show how to get arguments from the bag.
        Toast toast = Toast.makeText(context,
                "positive button clicked", Toast.LENGTH_LONG);
        toast.show();
        bag.clear(); //here you can do stuff with the bag and
        //adapter etc.
    }
    public void showDialog(View v) {
        //showing our dialog.

        dialog = new deleteFragment();
        //Here we show the dialog
        //The tag "MyFragement" is not important for us.
        dialog.show(getFragmentManager(), "MyFragment");
    }
    public static class MyDialog extends deleteFragment {


        @Override
        protected void negativeClick() {
            //Here we override the method and can now do something
            Toast toast = Toast.makeText(context,
                    "negative button clicked", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.addButton);
        dButton = (Button) findViewById(R.id.deleteSelctedButton);
        cButton = (Button) findViewById(R.id.clearListButton);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("saveBag"))
                bag = savedInstanceState.getParcelableArrayList("saveBag");
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int remove = listView.getCheckedItemPosition();
                if (remove >= 0) {
                    bag.remove(remove);
                    getMyAdapter().notifyDataSetChanged();
                }
                listView.clearChoices();

            }
        });
        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bag.clear();
                getMyAdapter().notifyDataSetChanged();
            }
        });
        //getting our listiew - you can check the ID in the xml to see that it
        //is indeed specified as "list"
        listView = (ListView) findViewById(R.id.list);
        //here we create a new adapter linking the bag and the
        //listview
        adapter = new ArrayAdapter<Product>(this,
                android.R.layout.simple_list_item_checked, bag);

        //setting the adapter on the listview
        listView.setAdapter(adapter);
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
                bag.add(new Product(value, qValue));
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
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

