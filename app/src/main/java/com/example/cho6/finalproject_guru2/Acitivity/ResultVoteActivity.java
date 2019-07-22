package com.example.cho6.finalproject_guru2.Acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;
import com.example.cho6.finalproject_guru2.adapter.UserVoteResultAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultVoteActivity extends AppCompatActivity {

    public ListView mListView;
    private TextView mTxtTitle, mTxtEx;
    public UserVoteResultAdapter mUserVoteResultAdapter;
    private VoteBean mVoteBean;
    public Button mbtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_vote);
        mbtnBack=findViewById(R.id.btnBack);
        mTxtTitle = findViewById(R.id.txtTitle);
        mTxtEx = findViewById(R.id.txtVoteEx);
        mListView = findViewById(R.id.lstResultChoice);

        mVoteBean = (VoteBean) getIntent().getSerializableExtra(VoteBean.class.getName());

        mTxtTitle.setText(mVoteBean.voteTitle);
        mTxtEx.setText(mVoteBean.voteSubTitle);
        mbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ResultVoteActivity.this, UserMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase.getInstance().getReference().child("votes").child(mVoteBean.voteID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                VoteBean voteBean = dataSnapshot.getValue(VoteBean.class);

                //실시간 데이터 세팅
                mUserVoteResultAdapter = new UserVoteResultAdapter(ResultVoteActivity.this, voteBean);
                mListView.setAdapter(mUserVoteResultAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

}
