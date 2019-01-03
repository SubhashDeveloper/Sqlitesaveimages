package com.example.subhash.sqlitesaveimages;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private EditText fname;
    private ImageView pic;
    private DatabaseHandler db;
    private String f_name;
//    private ListView lv;
    private dataAdapter data;
    private DataModel dataModel;
    private Bitmap bp;
    private byte[] photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Instantiate database handler
        db=new DatabaseHandler(this);

//        lv = (ListView) findViewById(R.id.list1);
        pic= (ImageView) findViewById(R.id.pic);
        fname=(EditText) findViewById(R.id.txt1);

    }
    public void buttonClicked(View v){
        int id=v.getId();

        switch(id){

            case R.id.save:

                if(fname.getText().toString().trim().equals("")){
                    fname.setError("Enter name");
                }
                if ("not_image".equals(pic.getTag()))
                {
                    Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                }
                else{
                    addContact();
                }

                break;

            case R.id.display:
                ShowRecords();
                break;
            case R.id.pic:
                pic.setTag("image");
                selectImage();
                break;
        }
    }

    private void ShowRecords() {
        startActivity(new Intent(getApplicationContext(),Main2Activity.class));
    }

    public void selectImage(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 2:
                if(resultCode == RESULT_OK){
                    Uri choosenImage = data.getData();

                    if(choosenImage !=null){

//                        bp=decodeUri(choosenImage, 400);
                        //                        bp=decodeUri(choosenImage, 400);
                        InputStream inputStream = null;
                        try {
                            inputStream = getContentResolver().openInputStream(choosenImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        bp = BitmapFactory.decodeStream(inputStream);
                        pic.setImageBitmap(bp);
                        pic.setImageBitmap(bp);
                    }
                }
        }
    }


    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //Convert bitmap to bytes
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }



    // function to get values from the Edittext and image
    private void getValues(){
        f_name = fname.getText().toString();
        photo = profileImage(bp);
    }

    //Insert data to the database
    private void addContact(){
        getValues();

        db.addContacts(new DataModel(f_name, photo));
        Toast.makeText(getApplicationContext(),"Saved successfully", Toast.LENGTH_LONG).show();
        pic.setImageResource(R.drawable.ic_launcher_background);
        fname.setText("");
        pic.setTag("not_image");
    }




}
