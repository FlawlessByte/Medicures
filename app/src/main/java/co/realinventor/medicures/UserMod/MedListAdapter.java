package co.realinventor.medicures.UserMod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.R;

public class MedListAdapter extends ArrayAdapter<Medicine> {
    private ArrayList<Medicine> medList;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        public TextView medName;
        TextView medDosage;
        TextView medQty;
    }

    public MedListAdapter (ArrayList<Medicine> medList, Context mContext){
        super(mContext, R.layout.med_list_item, medList);
        this.mContext = mContext;
        this.medList = medList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.med_list_item, null);
        }

        Medicine med = medList.get(position);

        if(med != null){
            TextView medName = view.findViewById(R.id.listMedName);
            TextView medDosage = view.findViewById(R.id.listMedDosage);
            TextView medQty = view.findViewById(R.id.listMedQty);

            medName.setText(medList.get(position).getMedicine_name());
            medDosage.setText(medList.get(position).getDosage());
            medQty.setText(medList.get(position).getQuantity());

        }



        return  view;
    }
}
