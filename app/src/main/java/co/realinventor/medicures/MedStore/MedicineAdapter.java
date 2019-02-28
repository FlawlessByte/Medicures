package co.realinventor.medicures.MedStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {

    private List<Medicine> medicineList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customerNameText, transIDText, dateIDText;

        public MyViewHolder(View view) {
            super(view);
            customerNameText = (TextView) view.findViewById(R.id.customerNameText);
            transIDText = (TextView) view.findViewById(R.id.transIDText);
            dateIDText = (TextView) view.findViewById(R.id.dateIDText);
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
        holder.customerNameText.setText("Customer : "+medicine.getCustomerName());
        holder.transIDText.setText("Trans ID : "+medicine.getTrans_id());
        holder.transIDText.setText("Date : "+medicine.date);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
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
