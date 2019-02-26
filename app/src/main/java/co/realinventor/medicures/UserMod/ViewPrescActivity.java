package co.realinventor.medicures.UserMod;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.Common.PrescDetails;
import co.realinventor.medicures.R;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

public class ViewPrescActivity extends AppCompatActivity {
    private ImageView imageView;
    String uri;
    private PrescDetails presc;
    private ArrayList<String> docPath = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_presc);

        imageView = findViewById(R.id.prescImageView);
        presc =(PrescDetails) getIntent().getSerializableExtra("presc");

        FilePickerBuilder.Companion.getInstance()
                .setMaxCount(1)
                .setActivityTitle("Select A Prescription")
                .setActivityTheme(R.style.LibAppTheme)
                .sortDocumentsBy(SortingTypes.name)
                .pickPhoto(this);





    }

    public void proceedButtonClicked(View view){
        Intent i = new Intent(this, AddressActivity.class);
        i.putExtra("uri",uri);
        i.putExtra("presc", presc);
        startActivity(i);
    }

    public void cancelButtonClicked(View view){
        finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("ActivityResult", resultCode + "");
        if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                docPath.clear();
                docPath.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                uri = docPath.get(0);

                Log.d("Uri", uri);

                File imgFile = new  File(docPath.get(0));

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imageView.setImageBitmap(myBitmap);
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
