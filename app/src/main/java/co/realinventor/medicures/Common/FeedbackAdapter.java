package co.realinventor.medicures.Common;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.AdminMod.AdminReplyActivity;
import co.realinventor.medicures.R;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.MyViewHolder> {

    private List<Feedback> feedbackList;
    public Context context;

    public FeedbackAdapter(List<Feedback> feedbackList){
        this.feedbackList = feedbackList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg,from,date;
        public Button replyButton;

        public MyViewHolder(View view) {
            super(view);
            msg = (TextView) view.findViewById(R.id.msg);
            from = (TextView) view.findViewById(R.id.from);
            date = (TextView) view.findViewById(R.id.date);
            replyButton = (Button) view.findViewById(R.id.replyButton);
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
        final Feedback feedback = feedbackList.get(position);
        holder.msg.setText(feedback.getMsg());
        holder.date.setText(feedback.getDateNTime());
        holder.from.setText(feedback.senderName);
        if(feedback.getTo().equals("admin@medcure")){
            holder.replyButton.setVisibility(View.VISIBLE);
            holder.replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AdminReplyActivity.class);
                    intent.putExtra("to", feedback.getFrom());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
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



