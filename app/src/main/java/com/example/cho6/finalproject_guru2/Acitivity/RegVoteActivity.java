package com.example.cho6.finalproject_guru2.Acitivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cho6.finalproject_guru2.Bean.ChoiceBean;
import com.example.cho6.finalproject_guru2.Bean.VoteBean;
import com.example.cho6.finalproject_guru2.R;
import com.example.cho6.finalproject_guru2.adapter.ChoiceAdapter;
import com.example.cho6.finalproject_guru2.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class RegVoteActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    //객체 선언
    private int mYear, mMonth, mDay, mHour, mMinute;
    private TextView mTxtStartDate, mTxtStartTime, mTxtEndDate, mTxtEndTime;
    private EditText mEdtTitle, mEdtDetail, mItem1, mItem2;
    private Button mBtnStartDate, mBtnStartTime, mBtnEndDate, mBtnEndTime, mBtnReg;
    private Switch mSwitchPublic;
    private CheckBox mCheckOverlap;
    private Context mContext;
    private EditText mEdtCode;
    private ImageView mImgLock;
    private ListView mLstChoice;
    private ChoiceAdapter mChoiceAdapter;
    private List<ChoiceBean> mChoiceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_vote2);

        //텍스트뷰 두개 연결
        mBtnStartDate = findViewById(R.id.btnStartDate);
        mBtnStartTime = findViewById(R.id.btnStartTime);
        mBtnEndDate = findViewById(R.id.btnEndDate);
        mBtnEndTime = findViewById(R.id.btnEndTime);

        mTxtStartDate = findViewById(R.id.txtStartDate);
        mTxtStartTime = findViewById(R.id.txtStartTime);
        mTxtEndDate = findViewById(R.id.txtEndDate);
        mTxtEndTime = findViewById(R.id.txtEndTime);

        mLstChoice = findViewById(R.id.lstChoice);

        mSwitchPublic = findViewById(R.id.switchPublic);
        mCheckOverlap = findViewById(R.id.checkBox);

        //객체 선언
        mEdtTitle = findViewById(R.id.edtTitle);
        mEdtDetail = findViewById(R.id.edtDetail);
        mItem1 = findViewById(R.id.item1);
        mItem2 = findViewById(R.id.item2);
        mEdtCode = findViewById(R.id.edtCode);

        mBtnReg = findViewById(R.id.btnReg);
        mBtnStartDate.setOnClickListener(mClicks);
        mBtnStartTime.setOnClickListener(mClicks);
        mBtnEndDate.setOnClickListener(mClicks);
        mBtnEndTime.setOnClickListener(mClicks);
        findViewById(R.id.btnadditem).setOnClickListener(mClicks);


        mBtnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(RegVoteActivity.this);
                alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addVote();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("투표를 생성하시겠습니까?");
                alert.show();

            }
        });

        mSwitchPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    mEdtCode.setVisibility(View.VISIBLE);
                } else {
                    mEdtCode.setVisibility(View.GONE);
                }
            }
        });

        mCheckOverlap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //중복체크 설정값 변경
                mChoiceAdapter.chnageChoiceItemOverlap(isChecked);
            }
        });

        //현재 날짜와 시간을 가져오기 위한 Calandar 인스턴스 선언
        Calendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);


        //처음 4개만 초기 데이터로 셋팅한다.
        mChoiceList = new ArrayList<>();
        mChoiceList.add(new ChoiceBean(false, ""));
        mChoiceList.add(new ChoiceBean(false, ""));
        mChoiceList.add(new ChoiceBean(false, ""));
        mChoiceList.add(new ChoiceBean(false, ""));

        mChoiceAdapter = new ChoiceAdapter(this, mChoiceList, false, true);
        mLstChoice.setAdapter(mChoiceAdapter);

    } //end onCreate()

    private View.OnClickListener mClicks = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //날짜 대화상자 버튼이 눌리면 대화상자를 보여줌
                case R.id.btnStartDate:
                    //리스너 등록
                    new DatePickerDialog(RegVoteActivity.this, mStartDateSetListner, mYear, mMonth, mDay).show();
                    break;
                case R.id.btnStartTime:
                    //리스너 등록
                    new TimePickerDialog(RegVoteActivity.this, mStartTimeSetListner, mHour, mMinute, false).show();
                    break;
                case R.id.btnEndDate:
                    new DatePickerDialog(RegVoteActivity.this, mEndDateSetListner, mYear, mMonth, mDay).show();
                    break;
                case R.id.btnEndTime:
                    new TimePickerDialog(RegVoteActivity.this, mEndTimeSetListner, mHour, mMinute, false).show();
                    break;
                case R.id.btnadditem:
                    //아이템 추가
                    mChoiceAdapter.addChoiceItem();
                    break;
            }
        }

    };

    //날짜 대화상자 리스너 부분
    DatePickerDialog.OnDateSetListener mStartDateSetListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    //텍스트 뷰의 값 업테이트
                    UpdateStart();
                }
            };
    //시간 대화상자 리스너 부분
    TimePickerDialog.OnTimeSetListener mStartTimeSetListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourofDay, int minute) {
                    //사용자가 입력한 값 가져옴
                    mHour = hourofDay;
                    mMinute = minute;

                    UpdateStart();
                }
            };

    //날짜 대화상자 리스너 부분
    DatePickerDialog.OnDateSetListener mEndDateSetListner =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    //텍스트 뷰의 값 업테이트
                    UpdateEnd();
                }
            };
    //시간 대화상자 리스너 부분
    TimePickerDialog.OnTimeSetListener mEndTimeSetListner =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourofDay, int minute) {
                    //사용자가 입력한 값 가져옴
                    mHour = hourofDay;
                    mMinute = minute;

                    UpdateEnd();
                }
            };

    private void UpdateStart() {
        mTxtStartDate.setText(String.format("%d-%02d-%02d", mYear, mMonth + 1, mDay));
        mTxtStartTime.setText(String.format("%d:%d:00", mHour, mMinute));
    }

    private void UpdateEnd() {
        mTxtEndDate.setText(String.format("%d-%02d-%02d", mYear, mMonth + 1, mDay));
        mTxtEndTime.setText(String.format("%d:%d:00", mHour, mMinute));
    }

    //투표추가
    private void addVote() {
        //Toast.makeText(this, "addVote()실행", Toast.LENGTH_LONG).show();

        List<ChoiceBean> choiceList = mChoiceAdapter.getChoiceList();
        boolean isContinue = true;
        for (ChoiceBean bean : choiceList) {
            //하나라도 항목이 없으면 추가 못한다
            if (TextUtils.isEmpty(bean.itemTitle)) {
                isContinue = false;
                break;
            }
        }
        if (!isContinue || choiceList.size() == 0) {
            Toast.makeText(this, "내용이 없는 항목이 있습니다. 항목 내용을 채워 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTxtStartDate.getText().toString())) {
            Toast.makeText(this, "시작 날짜가 없습니다. 날짜를 설정해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTxtEndDate.getText().toString())) {
            Toast.makeText(this, "종료 날짜가 없습니다. 날짜를 설정해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        VoteBean voteBean = new VoteBean();
        //데이터베이스에 저장한다.
        voteBean.voteTitle = mEdtTitle.getText().toString();
        voteBean.voteSubTitle = mEdtDetail.getText().toString();
        voteBean.voteStartDate = mTxtStartDate.getText().toString();
        voteBean.voteStartTime = mTxtStartTime.getText().toString();
        voteBean.voteEndDate = mTxtEndDate.getText().toString();
        voteBean.voteEndTime = mTxtEndTime.getText().toString();
        voteBean.voteID = System.currentTimeMillis() + "";
        voteBean.adminId = Utils.getUserIdFromUUID(mFirebaseAuth.getCurrentUser().getEmail());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //시작시간
            voteBean.startVoteMilli = sdf.parse(voteBean.voteStartDate + " " + voteBean.voteStartTime).getTime();
            //voteBean.startVoteMilli = System.currentTimeMillis();
            //종료시간
            voteBean.endVoteMilli = sdf.parse(voteBean.voteEndDate + " " + voteBean.voteEndTime).getTime();

            if (voteBean.endVoteMilli < voteBean.startVoteMilli) {
                Toast.makeText(this, "투표 종료 시점이 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Choice 항목추가
        voteBean.choiceList = choiceList;

        //공개여부
        if (mSwitchPublic.isChecked()) {
            voteBean.Lock = true;
            voteBean.voteCode = mEdtCode.getText().toString();
        }

        //항목체크 중복허용 여부
        if (mCheckOverlap.isChecked()) {
            voteBean.overlap = true;
        } else {
            voteBean.overlap = false;
            voteBean.startVote = false;


        }
        //Firebase 데이터베이스에 투표를 등록한다.
        DatabaseReference dbRef = mFirebaseDatabase.getReference();
        dbRef.child("votes").child(voteBean.voteID).setValue(voteBean);

        Toast.makeText(this, "투표가 추가 되었습니다.", Toast.LENGTH_SHORT).show();

        finish();


    }
}