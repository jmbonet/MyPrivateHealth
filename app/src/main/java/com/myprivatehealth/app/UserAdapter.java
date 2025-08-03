package com.myprivatehealth.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    
    private List<User> userList;
    private UserManagementActivity activity;

    public UserAdapter(List<User> userList, UserManagementActivity activity) {
        this.userList = userList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUserName;
        private TextView tvUserDetails;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserDetails = itemView.findViewById(R.id.tv_user_details);
            btnEdit = itemView.findViewById(R.id.btn_edit_user);
            btnDelete = itemView.findViewById(R.id.btn_delete_user);
        }

        public void bind(final User user) {
            tvUserName.setText(user.getName());
            tvUserDetails.setText(user.getAge() + " years, " + user.getHeight() + " cm");

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.showEditUserDialog(user);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.showDeleteUserDialog(user);
                }
            });
        }
    }
} 