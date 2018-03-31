package translet.transletapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> mMessages;
    private int[] mUserColors;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mUnameView;
        public TextView mMessageView;
        public ViewHolder(View view) {
            super(view);
            mUnameView = (TextView) view.findViewById(R.id.uname);
            mMessageView = (TextView)view.findViewById(R.id.message_received);
        }

        public void setUname(String uname) {
            if(null == uname) return;
            mUnameView.setText(uname);
            mUnameView.setTextColor(getUserColor(uname));
        }
        public void setMessage(String msg) {
            if(null == msg) return;
            mMessageView.setText(msg);
        }

        private int getUserColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUserColors.length);
            return mUserColors[index];
        }
    }
    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUserColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.activity_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message m = mMessages.get(position);
        viewHolder.setMessage(m.getMessage());
        viewHolder.setUname(m.getUsername());
    }

    @Override
    public int getItemCount() {return mMessages.size();}

}
