package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Movement;
import pedroadmn.example.organizzeclone.R;

public class MovementAdapter extends RecyclerView.Adapter<MovementAdapter.MyViewHolder> {

    List<Movement> movements;
    Context context;

    public MovementAdapter(List<Movement> movements, Context context) {
        this.movements = movements;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.movement_adapter, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movement movement = movements.get(position);

        holder.title.setText(movement.getDescription());
        holder.value.setText(String.valueOf(movement.getValue()));
        holder.category.setText(movement.getCategory());

        holder.value.setTextColor(context.getResources().getColor(R.color.colorAccentRevenue));

        if (movement.getType().equals("d")) {
            holder.value.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.value.setText("-" + movement.getValue());
        }
    }


    @Override
    public int getItemCount() {
        return movements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, value, category;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            value = itemView.findViewById(R.id.tvValue);
            category = itemView.findViewById(R.id.tvCategory);
        }
    }
}
