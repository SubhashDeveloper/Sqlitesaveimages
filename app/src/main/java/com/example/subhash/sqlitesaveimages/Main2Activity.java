package com.example.subhash.sqlitesaveimages;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    ListView listView;
    private DataModel dataModel;
    private DatabaseHandler db;
    private dataAdapter data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView=findViewById(R.id.list1);
        dataModel =new DataModel();
        //Instantiate database handler
        db=new DatabaseHandler(this);
        ShowRecords();

    }
     //Retrieve data from the database and set to the list view
     //Retrieve data from the database and set to the list view
     private void ShowRecords(){
         final ArrayList<DataModel> dataModels = new ArrayList<>(db.getAllContacts());
         data=new dataAdapter(this, dataModels);

         listView.setAdapter(data);

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 dataModel = dataModels.get(position);

                 Toast.makeText(getApplicationContext(),String.valueOf(dataModel.getID()), Toast.LENGTH_SHORT).show();
             }
         });
     }
}
