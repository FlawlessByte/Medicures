package co.realinventor.medicures.Common;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private List<Feedback> feedbackList;

    public FeedbackAdapter(List<Feedback> feedbackList){
        this.feedbackList = feedbackList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg,from,date;

        public MyViewHolder(View view) {
            super(view);
            msg = (TextView) view.findViewById(R.id.msg);
            from = (TextView) view.findViewById(R.id.from);
            date = (TextView) view.findViewById(R.id.date);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feedback_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.msg.setText(feedback.getMsg());
        holder.date.setText(feedback.getDateTime());
        holder.from.setText(feedback.getFrom());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }


}



