package com.example.cho6.finalproject_guru2.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cho6.finalproject_guru2.Acitivity.ResultVoteActivity;
import com.example.cho6.finalproject_guru2.Acitivity.VoteActivity;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private List<VoteBean> mVoteList;


    public UserAdapter(Context context, List <VoteBean> voteList){
        mContext = context;
        mVoteList = voteList;
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
        view = inflater.inflate(R.layout.view_vote_user, null);

        TextView txtVote = view.findViewById(R.id.txtVoteName);
        TextView txtVoteEx = view.findViewById(R.id.txtVoteEx);
        TextView txtStart = view.findViewById(R.id.txtStart);
        TextView txtEnd=view.findViewById(R.id.txtEnd);
        Button btnVote = view.findViewById(R.id.btnVote);
        Button btnShowResult = view.findViewById(R.id.btnShowResult);
        ImageView imgLock = view.findViewById(R.id.imgLock);


        final VoteBean voteBean = mVoteList.get(i);

        txtVote.setText(voteBean.voteTitle);
        txtStart.setText("시작: "+voteBean.voteStartDate+" "+voteBean.voteStartTime);
        txtEnd.setText("종료: "+voteBean.voteEndDate+" "+voteBean.voteEndTime);
        txtVoteEx.setText(voteBean.voteSubTitle);

        if(voteBean.Lock == true){
            imgLock.setVisibility(View.VISIBLE);
        }
        //결과보기
        btnShowResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(voteBean.voteCode) ){
                    Intent i = new Intent(mContext, ResultVoteActivity.class);
                    i.putExtra(VoteBean.class.getName(), voteBean);
                    mContext.startActivity(i);
                }else {
                    final Dialog dlg = new Dialog(mContext);
                    dlg.setContentView(R.layout.view_custom_dialog);
                    dlg.show();
                    final EditText edtCode = dlg.findViewById(R.id.edtCode);
                    Button btnOk = dlg.findViewById(R.id.btnOK);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String code = edtCode.getText().toString();
                            if (TextUtils.equals(voteBean.voteCode, code)) {

                                Intent i = new Intent(mContext, ResultVoteActivity.class);
                                i.putExtra(VoteBean.class.getName(), voteBean);
                                mContext.startActivity(i);
                                dlg.dismiss();
                            } else {
                                Toast.makeText(mContext, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        //투표하기
        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if( TextUtils.isEmpty(voteBean.voteCode) ){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        long end = sdf.parse(voteBean.voteEndDate+" "+voteBean.voteEndTime).getTime();

                        if(end > now){
                            Intent i=new Intent(mContext,VoteActivity.class);
                            i.putExtra(VoteBean.class.getName(), voteBean);
                            mContext.startActivity(i);
                        }else {
                            Toast.makeText(mContext, "투표가 종료되었습니다.", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }else{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        long end = sdf.parse(voteBean.voteEndDate+" "+voteBean.voteEndTime).getTime();

                        if(end > now){
                            final Dialog dlg = new Dialog(mContext);
                            dlg.setContentView(R.layout.view_custom_dialog);
                            dlg.show();
                            final EditText edtCode = dlg.findViewById(R.id.edtCode);
                            Button btnOk = dlg.findViewById(R.id.btnOK);
                            btnOk.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String code = edtCode.getText().toString();
                                    if(TextUtils.equals(voteBean.voteCode, code) ) {

                                        Intent i=new Intent(mContext,VoteActivity.class);
                                        i.putExtra(VoteBean.class.getName(), voteBean);
                                        mContext.startActivity(i);
                                        dlg.dismiss();
                                    }
                                    else {
                                        Toast.makeText(mContext, "코드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else {
                            Toast.makeText(mContext, "투표가 종료되었습니다.", Toast.LENGTH_LONG).show();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        return view;
    }
}