package com.yisan.addressdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.yisan.addressdemo.bean.Person;
import com.yisan.addressdemo.view.IndexView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 仿钉钉通讯录
 *
 * @author wzh
 * @packageName com.yisan.dingdingaddressdemo
 * @fileName MainActivity.java
 * @date 2020-04-13  上午 9:47
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvHint;
    private IndexView indexView;
    private Handler handler = new Handler(Looper.getMainLooper());
    private RecyclerView rv;
    private Map<String, List<Person>> addressMap = new HashMap<>();
    private AddressAdapter addressAdapter;
    private List<SectionBean> sectionBeanList;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout llLayout;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
        initData();
    }

    private void initView() {
        tvHint = findViewById(R.id.tv_hint);
        indexView = findViewById(R.id.index_view);
        rv = findViewById(R.id.content);
        llLayout = findViewById(R.id.ll_layout);
        tvTitle = findViewById(R.id.tv_title);
    }

    private void initEvent() {
        indexView.setOnSelectChangeListener(new IndexView.OnSelectChangeListener() {
            @Override
            public void onSelectChange(String word) {

                tvHint.setVisibility(View.VISIBLE);
                tvHint.setText(word);

                int letterIndex = getLetterIndex(sectionBeanList, word);
                if (letterIndex < sectionBeanList.size()) {
                    rv.scrollToPosition(letterIndex);
                    if (letterIndex == 0){
                        llLayout.setVisibility(View.GONE);
                    }
                }

                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvHint.setText("");
                        tvHint.setVisibility(View.GONE);
                    }
                }, 1000);

            }
        });
    }

    private void initData() {

        sectionBeanList = transformData();
        addressAdapter = new AddressAdapter();
        addressAdapter.replaceData(sectionBeanList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(addressAdapter);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //滑动实时回调
                if (linearLayoutManager != null) {
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (sectionBeanList.size() > 0) {
                        SectionBean sectionBean = sectionBeanList.get(firstVisibleItemPosition);
                        if (sectionBean != null) {
                            if (sectionBean.isHeader) {
                                llLayout.setVisibility(View.VISIBLE);
                                tvTitle.setText(sectionBean.header);
                            }
                            if (firstVisibleItemPosition <= 0) {
                                llLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                }

            }
        });

    }

    private List<SectionBean> transformData() {

        List<SectionBean> sectionBeans = new ArrayList<>();
        List<Person> persons = Person.getData();
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (!TextUtils.isEmpty(person.getHeadPinYin())) {
                List<Person> list = addressMap.get(person.getHeadPinYin());
                if (list != null) {
                    list.add(person);
                } else {
                    list = new ArrayList<>();
                    list.add(person);
                }
                addressMap.put(person.getHeadPinYin(), list);
            }
        }

        if (addressMap.size() > 0) {
            for (int i = 'A'; i <= 'Z'; i++) {
                String letter = String.valueOf((char) i);
                if (addressMap.containsKey(letter)) {
                    List<Person> personList = addressMap.get(letter);
                    //标题
                    sectionBeans.add(new SectionBean(true, letter));
                    if (personList != null) {
                        for (int j = 0; j < personList.size(); j++) {
                            Person person = personList.get(j);
                            sectionBeans.add(new SectionBean(person));
                        }
                    }
                }
            }
        }
        return sectionBeans;
    }

    public int getLetterIndex(List<SectionBean> list, String letter) {
        int currentIndex = 0;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SectionBean bean = list.get(i);
                if (bean.isHeader) {
                    if (bean.header != null && bean.header.equals(letter)) {
                        currentIndex = i;
                        break;
                    }
                }
            }
        }
        return currentIndex;
    }


    class AddressAdapter extends BaseSectionQuickAdapter<SectionBean, BaseViewHolder> {

        public AddressAdapter() {
            super(R.layout.address_list_item, R.layout.address_list_title_item, new ArrayList<SectionBean>());
        }

        @Override
        protected void convertHead(BaseViewHolder helper, SectionBean item) {
            helper.setText(R.id.title, item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, SectionBean item) {
            try {
                helper.setText(R.id.header, item.t.getHeadPinYin());
                helper.setText(R.id.name, item.t.name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    class SectionBean extends SectionEntity<Person> {

        public SectionBean(boolean isHeader, String header, Person person) {
            super(isHeader, header);
            this.t = person;
        }

        public SectionBean(boolean isHeader, String header) {
            super(isHeader, header);
        }

        public SectionBean(Person person) {
            super(person);
        }
    }

}
