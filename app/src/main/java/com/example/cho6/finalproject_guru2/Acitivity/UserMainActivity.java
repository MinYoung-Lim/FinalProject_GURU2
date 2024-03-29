package com.example.cho6.finalproject_guru2.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.adapter.UserAdapter;
import com.example.cho6.finalproject_guru2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    private static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

    private String mTxtEndDate, mTxtEndTime;

    private long now = System.currentTimeMillis();

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDB = FirebaseDatabase.getInstance();

    private ListView mListView;

    //원본 데이터
    private List<VoteBean> mVoteList = new ArrayList<>();
    //어뎁터 생성및 적용
    private UserAdapter mUserAdapter;
    private VoteBean voteBean;

    private boolean isMyVoted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.LstUser);



        //최초 데이터 세팅
        mUserAdapter = new UserAdapter(this, mVoteList);
        mListView.setAdapter(mUserAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mFirebaseDB.getReference().child("votes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //데이터를 받아와서 List에 저장
                mVoteList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VoteBean bean = snapshot.getValue(VoteBean.class);

                    if (bean.startVote == true && bean.endVote == false) {
                        mVoteList.add(0, bean);
                    } else if (bean.endVote) {
                        mVoteList.remove(bean);
                    }

                }
                //바뀐 데이터로 refresh 한다
                if (mUserAdapter != null) {
                    mUserAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

