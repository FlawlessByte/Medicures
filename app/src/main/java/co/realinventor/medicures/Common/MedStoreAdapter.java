package co.realinventor.medicures.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.MedStore.MedStoreDetails;
import co.realinventor.medicures.R;

public class MedStoreAdapter extends RecyclerView.Adapter<MedStoreAdapter.MyViewHolder> {

    private List<MedStoreDetails> medStoreList;

    public MedStoreAdapter(List<MedStoreDetails> medStoreList){
        this.medStoreList = medStoreList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mLocality;

        public MyViewHolder(View view) {
            super(view);
            mName = (TextView) view.findViewById(R.id.sMedName);
            mLocality = (TextView) view.findViewById(R.id.sMedLocality);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medstore_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MedStoreDetails medStoreDetails = medStoreList.get(position);
        holder.mName.setText(medStoreDetails.shopName);
        holder.mLocality.setText(medStoreDetails.locality);

    }

    @Override
    public int getItemCount() {
        return medStoreList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}