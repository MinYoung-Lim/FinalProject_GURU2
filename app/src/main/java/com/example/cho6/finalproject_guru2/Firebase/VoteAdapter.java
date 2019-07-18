package com.example.cho6.finalproject_guru2.Firebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cho6.finalproject_guru2.Bean.MemberBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.Database.FileDB;
import com.example.cho6.finalproject_guru2.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VoteAdapter extends BaseAdapter {

    private Context mContext;
    private List<VoteBean> mVoteList;
    LayoutInflater inflater;
    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    public VoteAdapter(Context context, List <VoteBean> voteList){
        mContext = context;
        mVoteList = voteList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mVoteList.size();
    }

    @Override
    public Object getItem(int i) {
        return mVoteList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_vote_admin, null);

        TextView txtVote = view.findViewById(R.id.txtVoteName);
        TextView txtVoteEx = view.findViewById(R.id.txtVoteEx);
        TextView txtStartDate = view.findViewById(R.id.txtDate);
        final Button btnStartVote = view.findViewById(R.id.btnStartVote);
        Button btnFinishVote = view.findViewById(R.id.btnFinishVote);
        Button btnShowVote = view.findViewById(R.id.btnShowVote);

        final VoteBean voteBean = mVoteList.get(i);

        txtVote.setText(voteBean.voteTitle);
        txtStartDate.setText(voteBean.voteStartDate);
        txtVoteEx.setText(voteBean.voteSubTitle);

        final VoteBean item = mVoteList.get(i);


        // 투표 시작 버튼이 눌리면 버튼 텍스트 변경
        if(voteBean.startVote == true) {
            btnStartVote.setText("투표중");
        } else
            btnStartVote.setText("투표시작");

        //투표 종료 버튼이 눌리면 버튼 텍스트 변경
         if(voteBean.endVote == true && voteBean.startVote == true) {
            btnFinishVote.setVisibility(view.GONE);




         } else
            btnFinishVote.setText("투표종료");

        //투표 시작 버튼 클릭 시 수행 될 작업
        btnStartVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteBean.startVote = true;
                DatabaseReference dbRef = mFirebaseDatabase.getReference();
                dbRef.child("votes").child(String.valueOf(voteBean.voteID)).setValue(voteBean);
            }
        });

        //투표 종료 버튼 클릭 시 수행 될 작업
        btnFinishVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 투표 시작 전에 종료를 누른 경우
                if(voteBean.startVote == false) {
                    Toast.makeText(mContext, "먼저 투표가 진행되어야 합니다.", Toast.LENGTH_LONG).show();
                    voteBean.endVote = false;
                    DatabaseReference dbRef = mFirebaseDatabase.getReference();
                    dbRef.child("votes").child(String.valueOf(voteBean.voteID)).setValue(voteBean);
                } else if(voteBean.startVote) {
                    voteBean.endVote = true;
                    DatabaseReference dbRef = mFirebaseDatabase.getReference();
                    dbRef.child("votes").child(String.valueOf(voteBean.voteID)).setValue(voteBean);

                   /* FileDB.delMemo(getActivity(), memberBean.memId, item.memoID);

                    mVoteList = FileDB.getMemoList(getActivity(), memberBean.memId);
                    notifyDataSetChanged(); //갱신해라*/



                }

                /* for(int i=0; i<mVoteList.size(); i++) {
                    VoteBean voteBean = mVoteList.get(i);
                    if(memberBean.isAdmin) {
                        mVoteList.remove(i);
                        break;
                    }
                }
                */

            }
        });

        return view;
    }
}
