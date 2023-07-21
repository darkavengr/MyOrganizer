package com.example.myorganizer;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.setTag;
import static com.example.myorganizer.MainActivity.recyclerView;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private static ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textDate;
        TextView textStartTime;
        TextView textEndTime;
        TextView textLocation;
        Button DeleteButton;
        Button EditButton;
        String Name;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textDate = (TextView) itemView.findViewById(R.id.textViewDate);
            this.textStartTime = (TextView) itemView.findViewById(R.id.textViewStartTime);
            this.textEndTime = (TextView) itemView.findViewById(R.id.textViewEndTime);
            this.textLocation = (TextView) itemView.findViewById(R.id.textViewLocation);
            this.DeleteButton=(Button) itemView.findViewById(R.id.deleteButton);

            Log.d("TEXTVIEW",String.valueOf(this.textName.getText()));
            this.Name= String.valueOf(this.textName.getText());

            this.DeleteButton.setOnClickListener(new CustomOnClickListener(this.Name) {
                @Override
                public void onClick(View view) {
                    Log.d("TEST","DELETE");

                    MainActivity.RemoveTask(getAdapterPosition());
                }
            });

            this.EditButton=(Button) itemView.findViewById(R.id.editButton);

            this.EditButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();

                    Intent intent = new Intent(view.getContext(), EditTask.class);
                    intent.putExtra("name",dataSet.get(position).name);
                    intent.putExtra("date",dataSet.get(position).date);
                    intent.putExtra("startTime",dataSet.get(position).starttime);
                    intent.putExtra("endTime",dataSet.get(position).endtime);
                    intent.putExtra("location",dataSet.get(position).location);

                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_list_item, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        DataModel model=dataSet.get(listPosition);

        holder.textName.setText(model.getName());
        holder.textDate.setText(model.getDate());
        holder.textStartTime.setText(model.getStartTime());
        holder.textEndTime.setText(model.getEndTime());
        holder.textLocation.setText(model.getLocation());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}

class CustomOnClickListener implements View.OnClickListener {
    String _name;


    CustomOnClickListener(String name) {
        _name=name;
    }

    @Override
    public void onClick(View view) {

    }
}
