package id.strade.android.seller.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.strade.android.seller.R;
import id.strade.android.seller.model.Message;
import id.strade.android.seller.model.Order;
import id.strade.android.seller.model.User;
import id.strade.android.seller.utils.DateTimeUtils;
import id.strade.android.seller.utils.Utils;

/**
 * Created by wahyu on 11 July 2017.
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<Message, ChatAdapter.MessageViewHolder> {
    Context context;
    Order order;
    private List<User> users;

    public ChatAdapter(Class<Message> modelClass, int modelLayout, Class<MessageViewHolder> viewHolderClass, Query ref, Context context, Order order) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.context = context;
        this.order = order;
        users = new ArrayList<>();
        users.add(order.buyer);
        users.add(order.seller);
    }

    @Override
    protected Message parseSnapshot(DataSnapshot snapshot) {
        Message message = super.parseSnapshot(snapshot);
        if (message != null) {
            message.id = snapshot.getKey();
        }
        return message;
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, Message message, int position) {
        setupMessageView(viewHolder, message);
        User user = getUser(message.user_id);
        Log.d("Wahyu", user.getImageUrl() + " " + user.getUsername());
        viewHolder.messengerTextView.setText(user.getFullName());
        viewHolder.messengerTextView.setTextColor(Utils.getUserColor(user.getUsername()));
        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(user.getImageUrl())
                    .into(viewHolder.messengerImageView);
        } else {
            viewHolder.messengerImageView.setImageDrawable(Utils.getDefaultImage(user.getFullName(), user.getUsername()));
        }

        configureTime(viewHolder, message, position);
    }


    private User getUser(int id) {
        User user = new User();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                user = users.get(i);
                break;
            }
        }
        return user;
    }

    private void setupMessageView(MessageViewHolder viewHolder, Message message) {
        if (message.imageUrl == null) {
            viewHolder.messageTextView.setText(message.text.trim());
            viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messageImageView.setVisibility(ImageView.GONE);
        } else {
            //sent image
            Glide.with(context)
                    .load(message.imageUrl)
                    .into(viewHolder.messageImageView);
            viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
            viewHolder.messageTextView.setVisibility(TextView.GONE);
        }
    }

    private void configureTime(MessageViewHolder viewHolder, Message message, int position) {
        Date date = new Date(message.timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int minute = calendar.get(Calendar.MINUTE);
        String clock = calendar.get(Calendar.HOUR_OF_DAY) + ":" + (minute < 10 ? "0" + minute : minute) + " WIB";
        viewHolder.timeTextView.setText(clock);

        Calendar today = Calendar.getInstance();
        String dateStr = DateTimeUtils.getLocalDate(calendar.getTime());
        String dateTodayStr = DateTimeUtils.getLocalDate(today.getTime());
        if (dateStr.equals(dateTodayStr)) dateStr = "Hari ini";
        viewHolder.dateTextView.setText(dateStr);

        compareWithPreviousMessage(viewHolder, message, position, dateStr);
    }

    private void compareWithPreviousMessage(MessageViewHolder viewHolder, Message message, int position, String dateStr) {
        if (position > 0 && getItem(position - 1).getDateStr().equals(dateStr)) {
            viewHolder.dateTextView.setVisibility(View.GONE);
        } else {
            viewHolder.dateTextView.setVisibility(View.VISIBLE);
        }

        if (position > 0 && getItem(position - 1).user_id == message.user_id && viewHolder.dateTextView.getVisibility() != View.VISIBLE) {
            viewHolder.messengerImageView.setVisibility(View.GONE);
            viewHolder.messengerTextView.setVisibility(View.GONE);
        } else {
            viewHolder.messengerImageView.setVisibility(View.VISIBLE);
            viewHolder.messengerTextView.setVisibility(View.VISIBLE);
        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        TextView timeTextView;
        TextView dateTextView;
        CircleImageView messengerImageView;

        public MessageViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
            messageImageView = (ImageView) itemView.findViewById(R.id.message_image_view);
            messengerTextView = (TextView) itemView.findViewById(R.id.messenger_text_view);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messenger_image_view);
            timeTextView = (TextView) itemView.findViewById(R.id.time);
            dateTextView = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
