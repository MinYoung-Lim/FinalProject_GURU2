package com.example.cho6.finalproject_guru2.Firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.cho6.finalproject_guru2.Bean.MemberBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EndVoteAdapter extends BaseAdapter {

    private Context mContext;
    private List<VoteBean> mEndVoteList;
    private MemberBean memberBean;

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public EndVoteAdapter(Context context, List <VoteBean> EndVoteList){
        mContext = context;
        mEndVoteList = EndVoteList;
    }

    @Override
    public int getCount() {
        return mEndVoteList.size();
    }

    @Override
    public Object getItem(int i) {
        return mEndVoteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_end_vote_admin, null);

        TextView txtVote = view.findViewById(R.id.txtVoteName);
        TextView txtVoteEx = view.findViewById(R.id.txtVoteEx);
        TextView txtStartDate = view.findViewById(R.id.txtDate);
        Button btnShowVote = view.findViewById(R.id.btnShowVote);

        final VoteBean voteBean = mEndVoteList.get(i);

        txtVote.setText(voteBean.voteTitle);
        txtStartDate.setText(voteBean.voteStartDate);
        txtVoteEx.setText(voteBean.voteSubTitle);

        return view;
    }
}
