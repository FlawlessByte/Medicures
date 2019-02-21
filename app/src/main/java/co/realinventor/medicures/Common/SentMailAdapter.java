package co.realinventor.medicures.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.MedStore.Medicine;
import co.realinventor.medicures.MedStore.MedicineAdapter;
import co.realinventor.medicures.R;

public class SentMailAdapter extends RecyclerView.Adapter<SentMailAdapter.MyViewHolder> {
    private List<Object> allItemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sentMailTo, sentMailMsg, sentMailDate;

        public MyViewHolder(View view) {
            super(view);
            sentMailTo = (TextView) view.findViewById(R.id.inputSentMailTo);
            sentMailMsg = (TextView) view.findViewById(R.id.inputSentMailMsg);
            sentMailDate = (TextView) view.findViewById(R.id.inputSentMailDate);
        }
    }

    public SentMailAdapter(List<Object> allItemList) {
        this.allItemList = allItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sent_mail_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Object object = allItemList.get(position);
        if(object instanceof Feedback){
            holder.sentMailTo.setText("To : "+((Feedback) object).getTo());
            holder.sentMailDate.setText("Date : "+((Feedback) object).getDateTime());
            holder.sentMailMsg.setText("Message : "+((Feedback) object).getMsg());
        }
        else if(object instanceof Notifications){
            holder.sentMailTo.setText("To : "+((Notifications) object).getTo());
            holder.sentMailDate.setText("Date : "+((Notifications) object).getDate()+" | "+((Notifications) object).getTime());
            holder.sentMailMsg.setText("Message : "+((Notifications) object).getMsg());

        }
        else if(object instanceof Medicine){
            holder.sentMailTo.setText("To : "+((Medicine) object).getTo());
            holder.sentMailDate.setText("Date : Not defined");
            Medicine med = (Medicine) object;
            holder.sentMailMsg.setText("Message : "+"Medicine Name : "+med.getMedicine_name()+"\nDosage : "+med.getDosage()+"\nQuantity : "+med.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return allItemList.size();
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
