package com.example.cho6.finalproject_guru2.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cho6.finalproject_guru2.Bean.EmailBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;
import com.example.cho6.finalproject_guru2.adapter.ShowPeopleAdapter;
import com.example.cho6.finalproject_guru2.adapter.UserVoteResultAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShowVotePeopleActivity extends AppCompatActivity {

    public ListView mListView;
    public ShowPeopleAdapter mShowPeopleAdapter;
    public VoteBean mVoteBean;
    public EmailBean mEmailBean;
    public List<EmailBean> mEmailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vote_people);

        mListView = findViewById(R.id.lstPeople);

        mVoteBean = (VoteBean) getIntent().getSerializableExtra(VoteBean.class.getName());
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase.getInstance().getReference().child("votes").child(mVoteBean.voteID).child("votedList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                VoteBean voteBean = dataSnapshot.getValue(VoteBean.class);
                EmailBean emailBean = dataSnapshot.getValue(EmailBean.class);


                try{
                    if(voteBean.votedList.size()>0) {
                        //자신이 투표한 리스트만 보이게 함
                        for(int i=0; i<voteBean.votedList.size();i++) {
                            mEmailBean.email = voteBean.votedList.get(i).userId;
                            mEmailList.add(0, mEmailBean);
                        }
                    }
                    throw new Exception(); //강제 에러 출력
                }catch (Exception e){
                    //에러시 수행
                    e.printStackTrace();
                }




                //실시간 데이터 세팅
                mShowPeopleAdapter = new ShowPeopleAdapter(ShowVotePeopleActivity.this, mEmailBean);
                mListView.setAdapter(mShowPeopleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}