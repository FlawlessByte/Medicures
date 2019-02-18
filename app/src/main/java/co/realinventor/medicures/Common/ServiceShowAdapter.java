package co.realinventor.medicures.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.AmbulanceService.ServiceDetails;
import co.realinventor.medicures.R;

public class ServiceShowAdapter extends RecyclerView.Adapter<ServiceShowAdapter.MyViewHolder> {

    private List<ServiceDetails> serviceDetailsList;

    public ServiceShowAdapter(List<ServiceDetails> serviceDetailsList){
        this.serviceDetailsList = serviceDetailsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mDriverName, mServiceLocality;

        public MyViewHolder(View view) {
            super(view);
            mDriverName = (TextView) view.findViewById(R.id.sDriverName);
            mServiceLocality = (TextView) view.findViewById(R.id.sServiceLocality);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceDetails serviceDetails = serviceDetailsList.get(position);
        holder.mDriverName.setText(serviceDetails.driverName);
        holder.mServiceLocality.setText(serviceDetails.driverLocality);

    }

    @Override
    public int getItemCount() {
        return serviceDetailsList.size();
    }


}