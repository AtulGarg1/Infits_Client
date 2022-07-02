package com.example.infits;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infits.databinding.ChatareaclientmessageBinding;
import com.example.infits.databinding.ChatareadietitianmessageBinding;
import com.example.infits.databinding.ChatareaclientmessageBinding;
import com.example.infits.databinding.ChatareadietitianmessageBinding;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        //extends RecyclerView.Adapter{

    private final List<ChatMessage> chatMessages;
    private final String senderID;
   // private final Context ct;
   // private final Bitmap receiverProfileImage;

    public static final int VIEW_TYPE_SENT=1;
    public static final int VIEW_TYPE_RECEIVE=2;
    public ChatMessageAdapter(List<ChatMessage> chatMessages, String senderID){// Bitmap receiverProfileImage) {
        this.chatMessages = chatMessages;
        this.senderID = senderID;
       // this.ct=ct;
      //  this.receiverProfileImage = receiverProfileImage;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==VIEW_TYPE_SENT){
           return new SentMessageViewHolder(
                   ChatareadietitianmessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)
           );
       }
       else{
           return new ReceivedMessageViewHolder(
                   ChatareaclientmessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false)
           );
       }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if(getItemViewType(position)== VIEW_TYPE_SENT) {
                ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
                //return new SentMessageViewHolder(View(holder));
            }
            else{
                ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position));

            }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).messageBy.equals("dietitian"))
            return VIEW_TYPE_SENT;
        else
            return VIEW_TYPE_RECEIVE;
    }


    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ChatareadietitianmessageBinding binding1;
        SentMessageViewHolder(ChatareadietitianmessageBinding chatAreaDietitianMessageBinding){
            super(chatAreaDietitianMessageBinding.getRoot());
            binding1=chatAreaDietitianMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding1.dietitianMsg.setText(chatMessage.getMessage());
            binding1.dietitianMsgTime.setText(chatMessage.getTime());
            binding1.dietitianMsg.setGravity(Gravity.RIGHT);
            binding1.dietitianMsgTime.setGravity(Gravity.RIGHT);
        }

    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final ChatareaclientmessageBinding binding2;
        ReceivedMessageViewHolder(ChatareaclientmessageBinding chatAreaClientMessageBinding){
            super(chatAreaClientMessageBinding.getRoot());
            binding2=chatAreaClientMessageBinding;
        }

        void setData(ChatMessage chatMessage){
            binding2.clientMsg.setText(chatMessage.message);
            binding2.clientMsgTime.setText(chatMessage.time);
        }
    }

}
