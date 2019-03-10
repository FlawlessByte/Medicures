package co.realinventor.medicures.BloodBank;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import co.realinventor.medicures.R;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.MyViewHolder> {
    private List<BloodDonor> donorList;
    public Context context;
    private String CALL_NUMBER;

    public BloodDonorAdapter(List<BloodDonor> donorList){
        this.donorList = donorList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView donorNameTextView, donorContactTextView, donorBloodGroupTextView;
        public ImageButton callButton;
        public ImageView profImg;

        public MyViewHolder(View view) {
            super(view);
            donorNameTextView = (TextView) view.findViewById(R.id.donorNameTextView);
            donorContactTextView = (TextView) view.findViewById(R.id.donorContactTextView);
            donorBloodGroupTextView = (TextView) view.findViewById(R.id.donorBloodGroupTextView);
            callButton = (ImageButton) view.findViewById(R.id.donorCallButton);
            profImg = (ImageView) view.findViewById(R.id.profImg);

        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BloodDonor donor = donorList.get(position);
        holder.donorNameTextView.setText(donor.getName());
        holder.donorContactTextView.setText("Contact : "+donor.getContact());
        holder.donorBloodGroupTextView.setText("Blood Group : "+donor.getBloodGroup());
        switch(donor.getGender()){
            case "Male":{
                holder.profImg.setImageResource(R.drawable.man);
                break;
            }
            case "Female":{
                holder.profImg.setImageResource(R.drawable.girl);
                break;
            }
            case "Other":{
                holder.profImg.setImageResource(R.drawable.thumbnail_profile);
                break;
            }
            default:
                break;
        }

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CALL_NUMBER = donor.getContact();
                showConfirmation();

            }
        });
    }


    private void showConfirmation(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Alert!")
                .setMessage("Are you sure you want to contact this person?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with call
                        callPhoneNumber();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

//                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    Toast.makeText(context, "No Call Permission!", Toast.LENGTH_SHORT).show();

                    return;
                }
            }

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel: "+CALL_NUMBER));
            context.startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return donorList.size();
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
