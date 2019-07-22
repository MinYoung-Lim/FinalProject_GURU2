package com.example.cho6.finalproject_guru2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cho6.finalproject_guru2.Acitivity.ResultVoteActivity;
import com.example.cho6.finalproject_guru2.Bean.MemberBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.Bean.VotedBean;
import com.example.cho6.finalproject_guru2.R;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UserVoteAdapter extends BaseAdapter {

    private Context mContext;
    private List<VoteBean> mUserVoteList;
    LayoutInflater inflater;

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public UserVoteAdapter(Context context, List<VoteBean> UserVoteList){
        mContext = context;
        mUserVoteList = UserVoteList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setList(List<VoteBean> userVoteList){
        mUserVoteList = userVoteList;
    }

    @Override
    public int getCount() {
        return mUserVoteList.size();
    }

    @Override
    public Object getItem(int i) {
        return mUserVoteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_my_vote_user, null);

        TextView txtVote = view.findViewById(R.id.txtVoteName);
        TextView txtVoteEx = view.findViewById(R.id.txtVoteEx);
        TextView txtStart = view.findViewById(R.id.txtStart);
        TextView txtEnd=view.findViewById(R.id.txtEnd);
        Button btnShowResult = view.findViewById(R.id.btnShowResult);
        ImageView imgLock = view.findViewById(R.id.imgLock);

        final VoteBean voteBean = mUserVoteList.get(i);

        txtVote.setText(voteBean.voteTitle);
        txtStart.setText("시작: "+voteBean.voteStartDate+" "+voteBean.voteStartTime);
        txtEnd.setText("종료: "+voteBean.voteEndDate+" "+voteBean.voteEndTime);
        txtVoteEx.setText(voteBean.voteSubTitle);

        if(voteBean.Lock== true){
            imgLock.setVisibility(View.VISIBLE);
        }

        btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ResultVoteActivity.class);
                i.putExtra(VoteBean.class.getName(), voteBean);
                mContext.startActivity(i);
            }
        });

        return view;
    }
}