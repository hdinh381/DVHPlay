package com.example.dvhplay.PlayVideo.Comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dvhplay.R;
import com.example.dvhplay.video.AdapterVideo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.ViewHoder> {
    List<ItemComment> commentList;

    public AdapterComment(List<ItemComment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public AdapterComment.ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comment_layout,parent,false);
        AdapterComment.ViewHoder viewHoder = new AdapterComment.ViewHoder(view);
        return viewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterComment.ViewHoder holder, int position) {
        ItemComment comment = commentList.get(position);
        holder.tvUserName.setText(comment.getUserName());
        holder.tvComment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvComment;
        public ViewHoder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
        }
    }
}
