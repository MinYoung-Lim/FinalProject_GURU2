package com.example.cho6.finalproject_guru2.Acitivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;
import com.example.cho6.finalproject_guru2.adapter.EndVoteAdapter;
import com.example.cho6.finalproject_guru2.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminVoteActivity extends AppCompatActivity {

    FirebaseDatabase mFirebaseDB = FirebaseDatabase.getInstance();
    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private List<VoteBean> mEndVoteList = new ArrayList<>();
    public EndVoteAdapter mEndVoteAdapter;
    public ListView mListView;

    @Override
    public void onResume() {
        super.onResume();

        mFirebaseDB.getReference().child("votes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String memId = Utils.getUserIdFromUUID(mFirebaseAuth.getCurrentUser().getEmail());
                mEndVoteList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VoteBean bean = snapshot.getValue(VoteBean.class);
                    if(bean.endVote == true && TextUtils.equals(memId, bean.adminId)) {
                        mEndVoteList.add(0, bean);
                    }
                    //바뀐 데이터로 refresh 한다
                    if (mEndVoteAdapter != null) {
                        mEndVoteAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_admin_vote);

        mListView = findViewById(R.id.manageVote);

        //최초 데이터 세팅
        mEndVoteAdapter = new EndVoteAdapter(this, mEndVoteList);
        mListView.setAdapter(mEndVoteAdapter);

    }
}
