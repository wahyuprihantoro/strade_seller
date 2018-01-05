package id.strade.android.seller.chat;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import id.strade.android.seller.R;
import id.strade.android.seller.model.Message;
import id.strade.android.seller.model.Order;
import id.strade.android.seller.storage.Prefs;


/**
 * Created by wahyu on 11 July 2017.
 */
@EActivity(R.layout.activity_chat)
public class ChatActivity extends AppCompatActivity {
    public static final String EMPTY_MESSAGE_WARNING = "pesan tidak boleh kosong";
    public static final String PERAKITAN = "Order";
    private static final String CHAT_FIREBASE_REFERENCE = "chat";
    @ViewById
    RecyclerView rv;
    @ViewById(R.id.in_message)
    EditText messageInput;
    @ViewById(R.id.image_send)
    ImageView sendButton;

    @Bean
    Prefs prefs;
    @Extra
    Order order;

    DatabaseReference databaseReference;
    ChatAdapter chatAdapter;
    LinearLayoutManager layoutManager;

    @AfterViews
    void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(PERAKITAN + " - " + order.getId());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        databaseReference = FirebaseDatabase.getInstance().getReference(CHAT_FIREBASE_REFERENCE);
        chatAdapter = new ChatAdapter(Message.class,
                R.layout.layout_message,
                ChatAdapter.MessageViewHolder.class,
                databaseReference.child(order.getId() + ""),
                this, order);
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = chatAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    rv.scrollToPosition(positionStart);
                }
            }
        });
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(chatAdapter);
        sendButton.setEnabled(!messageInput.getText().toString().isEmpty());
    }

    @AfterTextChange(R.id.in_message)
    public void afterTextChangedOnIputMessage(TextView ignored, Editable text) {
        sendButton.setEnabled(!text.toString().isEmpty());
    }

    @Click(R.id.image_send)
    public void send() {
        Message message = new Message();
        message.text = messageInput.getText().toString().trim();
        if (message.text.isEmpty()) {
            Toast.makeText(this, EMPTY_MESSAGE_WARNING, Toast.LENGTH_SHORT).show();
            return;
        }
        message.user_id = prefs.getUser().getId().intValue();
        message.timestamp = System.currentTimeMillis();
        databaseReference.child(order.getId() + "").push().setValue(message);
        messageInput.setText("");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
