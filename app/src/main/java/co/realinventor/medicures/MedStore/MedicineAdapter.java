package co.realinventor.medicures.MedStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.realinventor.medicures.R;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private List<Medicine> medicineList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customerNameText, transIDText;

        public MyViewHolder(View view) {
            super(view);
            customerNameText = (TextView) view.findViewById(R.id.customerNameText);
            transIDText = (TextView) view.findViewById(R.id.transIDText);
        }
    }


    public MedicineAdapter(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.medicine_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.customerNameText.setText(medicine.getCustomerName());
        holder.transIDText.setText(medicine.getTrans_id());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }
}
