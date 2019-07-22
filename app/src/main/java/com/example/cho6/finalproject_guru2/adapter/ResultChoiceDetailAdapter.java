package com.example.cho6.finalproject_guru2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.cho6.finalproject_guru2.Acitivity.ResultVoteDetailActivity;
import com.example.cho6.finalproject_guru2.Acitivity.ShowVotePeopleActivity;
import com.example.cho6.finalproject_guru2.Bean.ChoiceBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;

import java.util.List;

public class ResultChoiceDetailAdapter extends BaseAdapter {

    private Context mContext;
    public LayoutInflater inflater;
    public VoteBean mVoteBean;
    private List<ChoiceBean> mChoiceList;

    public ResultChoiceDetailAdapter(Context context, VoteBean voteBean) {
        mContext = context;
        mVoteBean = voteBean;
        mChoiceList = voteBean.choiceList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mChoiceList.size();
    }

    @Override
    public Object getItem(int i) {
        return mChoiceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_result_choice_detail, null);

        TextView txtNum = view.findViewById(R.id.txtNum);
        TextView txtChoiceTitle = view.findViewById(R.id.txtChoice);
        TextView txtVoteCount = view.findViewById(R.id.txtVoteCount);
        Button btnResultDetail = view.findViewById(R.id.btnResultDetail);

        final ChoiceBean choiceBean = mChoiceList.get(position);

        txtNum.setText( String.valueOf(position+1) );
        txtChoiceTitle.setText( choiceBean.itemTitle );
       // txtVoteCount.setText( String.valueOf(choiceBean.totalSelCount) + " 표" );
        txtVoteCount.setText(choiceBean.totalSelCount + "표");

        btnResultDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ShowVotePeopleActivity.class);
                i.putExtra(ChoiceBean.class.getName(), choiceBean);
                mContext.startActivity(i);
            }
        });
        return view;
    }

}
