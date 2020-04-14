package com.yisan.addressdemo.bean;

import com.yisan.addressdemo.utils.PinyinUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author：wzh
 * @description:
 * @packageName: com.yisan.dingdingaddressdemo.utils
 * @date：2020/4/14 0014 上午 10:53
 */
public class Person implements Serializable {

    public String name;

    public Person(String name) {
        this.name = name;
    }
    public String getHeadPinYin(){
        return PinyinUtil.getHeadByHanzi(name);
    }

    public static List<Person> getData() {

        List<Person> persons = new ArrayList<>();
        Person p1 = new Person("阿三");
        Person p2 = new Person("阿四");
        Person p3 = new Person("爱大爱");
        Person p4 = new Person("壁一");
        Person p5 = new Person("笔二");
        Person p6 = new Person("避三");
        Person p7 = new Person("必死");
        Person p8 = new Person("传递");
        Person p9 = new Person("地物");
        Person p10 = new Person("而来");
        Person p11 = new Person("附件");
        Person p12 = new Person("根据");
        Person p13 = new Person("狠人");
        Person p14 = new Person("ilav");
        Person p15 = new Person("唧唧");
        Person p16 = new Person("快快");
        Person p17 = new Person("聊聊");
        Person p18 = new Person("妹妹");
        Person p19 = new Person("奶奶");
        Person p20 = new Person("偶偶");
        Person p21 = new Person("pp");

        Person p22 = new Person("钱");
        Person p23 = new Person("热起来来嗯");
        Person p24 = new Person("势必");
        Person p25 = new Person("替替");
        Person p26 = new Person("u");
        Person p27 = new Person("V");
        Person p28 = new Person("我我");
        Person p29 = new Person("嘻嘻");
        Person p30 = new Person("嘤嘤嘤");
        Person p31 = new Person("啧啧啧啧啧啧做");
        Person p32= new Person("中中");

        Person p33= new Person("gg");
        Person p34= new Person("咯咯咯");
        Person p35= new Person("格格");
        Person p36= new Person("姐姐");
        Person p37= new Person("叽叽叽叽姐姐");
        Person p38= new Person("可可");

        persons.add(p9);
        persons.add(p2);
        persons.add(p1);
        persons.add(p36);
        persons.add(p3);
        persons.add(p34);
        persons.add(p38);
        persons.add(p5);
        persons.add(p6);
        persons.add(p4);
        persons.add(p33);
        persons.add(p37);
        persons.add(p8);
        persons.add(p10);
        persons.add(p7);
        persons.add(p17);
        persons.add(p18);
        persons.add(p35);
        persons.add(p19);
        persons.add(p20);
        persons.add(p21);
        persons.add(p11);
        persons.add(p12);
        persons.add(p13);
        persons.add(p14);
        persons.add(p15);
        persons.add(p16);
        persons.add(p22);
        persons.add(p27);
        persons.add(p28);
        persons.add(p29);
        persons.add(p23);
        persons.add(p31);
        persons.add(p24);
        persons.add(p25);
        persons.add(p26);
        persons.add(p30);
        persons.add(p32);

        //进行排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                //根据拼音进行排序
                return o1.getHeadPinYin().compareTo(o2.getHeadPinYin());
            }
        });

        return persons;
    }

}
