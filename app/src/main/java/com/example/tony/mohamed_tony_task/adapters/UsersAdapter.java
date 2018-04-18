package com.example.tony.mohamed_tony_task.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tony.mohamed_tony_task.RecyclerViewAnimator;
import com.example.tony.mohamed_tony_task.activities.MainActivity;
import com.example.tony.mohamed_tony_task.activities.ProfileActivity;
import com.example.tony.mohamed_tony_task.R;
import com.example.tony.mohamed_tony_task.webServices.responses.User;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> usersList = new ArrayList<>();
    public static String USER_OBJECT = "user_object";
    // Allows to remember the last item shown on screen
    private int lastPosition = -1;
    private UsersAdapter.OnButtonsItemClicked onButtonsItemClicked;

    private RecyclerViewAnimator mAnimator;

    public UsersAdapter(Context mContext,OnButtonsItemClicked onButtonsItemClicked ,List<User> usersList, RecyclerView recyclerView) {

        this.usersList = usersList;
        this.mContext = mContext;
        this.onButtonsItemClicked=onButtonsItemClicked;
        mAnimator = new RecyclerViewAnimator(recyclerView);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        /**
         * First item's entrance animations.
         */
        mAnimator.onCreateViewHolder(itemView);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.userName.setText(usersList.get(position).getContactName());
        holder.userEmail.setText(String.valueOf(usersList.get(position).getContactEmail()));

        mAnimator.onBindViewHolder(holder.itemView, position);

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        if(usersList!=null)
           return usersList.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView userName, userEmail;
        private ImageView userImage;
        private Button callButton, smsButton;

        public MyViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.user_name);
            userName.setSelected(true);
            userEmail = (TextView) view.findViewById(R.id.user_email);
            userImage = (ImageView) view.findViewById(R.id.user_circle_image);
            callButton=(Button)view.findViewById(R.id.call_button);
            smsButton=(Button)view.findViewById(R.id.send_button);


            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      onButtonsItemClicked.onCallClicked(getAdapterPosition());
                }
            });
            smsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                     onButtonsItemClicked.onSmsClicked(getAdapterPosition());
                }
            });


        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String contactInfo=usersList.get(position).getContactName()+" , "+usersList.get(position).getContactAddress()+" , "+usersList.get(position).getContactEmail();
            MainActivity.showTopSnakeBar(contactInfo);
           // Toast.makeText(mContext,, Toast.LENGTH_SHORT).show();

        }


        //======================================= on long item clicked move to the next screen =========================
        @Override
        public boolean onLongClick(View view) {

            int position = getAdapterPosition();
            Intent intent=new Intent(mContext,ProfileActivity.class);
            intent.putExtra(USER_OBJECT,usersList.get(position));
            mContext.startActivity(intent);

            return true;
        }
    }
    public interface OnButtonsItemClicked{
        public void onCallClicked(int position);
        public void onSmsClicked(int position);
    }
}
