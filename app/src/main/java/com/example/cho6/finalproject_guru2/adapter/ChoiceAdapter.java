package com.example.cho6.finalproject_guru2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.cho6.finalproject_guru2.Bean.ChoiceBean;
import com.example.cho6.finalproject_guru2.R;

import java.util.List;

public class ChoiceAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChoiceBean> mChoiceList;
    private boolean mIsOverlap;
    private boolean mIsMake;

    public ChoiceAdapter(Context context, List<ChoiceBean> choiceList, boolean isOverlap, boolean isMake) {
        mContext = context;
        mChoiceList = choiceList;
        mIsOverlap = isOverlap;
        mIsMake = isMake;
    }

    public void setChoiceList(List<ChoiceBean> choiceList) {
        mChoiceList = choiceList;
    }

    public List<ChoiceBean> getChoiceList() {
        return mChoiceList;
    }

    //항목추가
    public void addChoiceItem() {
        mChoiceList.add( new ChoiceBean(mIsOverlap, "") );
        notifyDataSetChanged();
    }

    //중복체크 설정값 변경
    public void chnageChoiceItemOverlap(boolean isOverlap) {
        mIsOverlap = isOverlap;
        //전체 리스트 리셋
        for(ChoiceBean choiceBean : mChoiceList) {
            choiceBean.isSelect = false;
        }
        notifyDataSetChanged();
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
        view = inflater.inflate(R.layout.view_choice, null);

        TextView txtNum = view.findViewById(R.id.txtNum);
        RadioButton rdoChoice = view.findViewById(R.id.rdoChoice);
        CheckBox chkChoice = view.findViewById(R.id.chkChoice);
        final EditText edtTile = view.findViewById(R.id.edtTitle);
        Button btnDel = view.findViewById(R.id.btnChoiceDel);

        final ChoiceBean choiceBean = mChoiceList.get(position);

        if(mIsMake) {
            rdoChoice.setVisibility(View.GONE);
            chkChoice.setVisibility(View.GONE);
        }
        else {
            if(mIsOverlap) {
                rdoChoice.setVisibility(View.GONE);
                chkChoice.setVisibility(View.VISIBLE);
                chkChoice.setChecked(choiceBean.isSelect);
            } else {
                rdoChoice.setVisibility(View.VISIBLE);
                chkChoice.setVisibility(View.GONE);
                rdoChoice.setChecked(choiceBean.isSelect);
            }
            edtTile.setEnabled(false);
            btnDel.setVisibility(View.GONE);
        }

        txtNum.setText( (position+1) + "." );
        edtTile.setText( choiceBean.itemTitle );


        rdoChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    for(int i=0; i<mChoiceList.size(); i++) {
                        ChoiceBean cBean = mChoiceList.get(i);
                        if(position == i) {
                            cBean.isSelect = true;
                        } else {
                            cBean.isSelect = false;
                        }
                        mChoiceList.set(i, cBean);
                    }
                    notifyDataSetChanged();
                }
            }
        });

        chkChoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                choiceBean.isSelect = b;
                mChoiceList.set(position, choiceBean);
            }
        });

        edtTile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                choiceBean.itemTitle = edtTile.getText().toString();
                mChoiceList.set(position, choiceBean);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("투표 항목을 삭제 하시겠습니까?");
                builder.setNegativeButton("아니오", null);
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mChoiceList.remove(position);
                        ChoiceAdapter.this.notifyDataSetChanged();
                    }
                });
                builder.create().show();
            }
        });

        return view;
    }

}
