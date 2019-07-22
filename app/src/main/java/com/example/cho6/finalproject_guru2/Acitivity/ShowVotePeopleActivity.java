package com.example.cho6.finalproject_guru2.Acitivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cho6.finalproject_guru2.Bean.ChoiceBean;
import com.example.cho6.finalproject_guru2.R;
import com.example.cho6.finalproject_guru2.adapter.ShowPeopleAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowVotePeopleActivity extends AppCompatActivity {

    private TextView mTxtTitle;
    public ListView mListView;
    public ShowPeopleAdapter mShowPeopleAdapter;
    public ChoiceBean mChoiceBean;
    public List<String> mEmailList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vote_people);

        mTxtTitle = findViewById(R.id.txtTitle);
        mListView = findViewById(R.id.lstPeople);

        mChoiceBean = (ChoiceBean) getIntent().getSerializableExtra(ChoiceBean.class.getName());

        //실시간 데이터 세팅
        mTxtTitle.setText("투표한 사람 총: " + mChoiceBean.totalSelCount + "명");
        if(mChoiceBean.selectUserIdList != null) {
            mShowPeopleAdapter = new ShowPeopleAdapter(ShowVotePeopleActivity.this, mChoiceBean.getSelectUserIdList());
            mListView.setAdapter(mShowPeopleAdapter);
        }
    }
}