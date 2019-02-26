package co.realinventor.medicures.UserMod;

import androidx.appcompat.app.AppCompatActivity;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewMedListActivity extends AppCompatActivity {
    ArrayList<Medicine> medList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_med_list);

        medList = (ArrayList<Medicine>) getIntent().getSerializableExtra("list");

        ListView listView = (ListView) findViewById(R.id.listViewMedicine);
        MedListAdapter adapter = new MedListAdapter(medList, getApplicationContext());
        listView.setAdapter(adapter);
    }

    public void proceedButtonClicked(View view){
        Intent i = new Intent(this, AddressActivity.class);
        i.putExtra("list",medList);
        startActivity(i);
    }

    public void cancelButtonClicked(View view){
        finish();
    }
}
