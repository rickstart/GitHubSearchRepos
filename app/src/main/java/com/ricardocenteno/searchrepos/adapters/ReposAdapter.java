package com.ricardocenteno.searchrepos.adapters;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ricardocenteno.searchrepos.R;
import com.ricardocenteno.searchrepos.models.Repository;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private List<Repository> objects;

    public ReposAdapter(List<Repository> objects) {
        this.objects = objects;
    }

    @NonNull
    @Override
    public ReposAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_repo,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReposAdapter.ViewHolder holder, int position) {
        Repository repository = objects.get(position);
        holder.txtRepo.setText(repository.getName());
        holder.txtStars.setText(""+repository.getStars());
        if (repository.getOwner()!=null && repository.getOwner().getAvatarUrl()!=null)
            Picasso.get().load(repository.getOwner().getAvatarUrl()).into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setObjects(List<Repository> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfile;
        TextView txtRepo;
        TextView txtStars;
        ViewHolder(View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtRepo = itemView.findViewById(R.id.txtRepo);
            txtStars = itemView.findViewById(R.id.txtStars);
        }
    }
}
