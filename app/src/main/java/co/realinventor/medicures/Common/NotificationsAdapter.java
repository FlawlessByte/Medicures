package co.realinventor.medicures.Common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private List<Notifications> notificationsList;

    public NotificationsAdapter(List<Notifications> notificationsList){
        this.notificationsList = notificationsList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg,from,date;

        public MyViewHolder(View view) {
            super(view);
            msg = (TextView) view.findViewById(R.id.textViewMsg);
            from = (TextView) view.findViewById(R.id.textViewName);
            date = (TextView) view.findViewById(R.id.textViewTimeDate);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notifications notifications = notificationsList.get(position);
        holder.msg.setText(notifications.getMsg());
        holder.date.setText("Time : " + notifications.getDate() + " | " + notifications.getTime());
        holder.from.setText(notifications.getSenderName());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }


}