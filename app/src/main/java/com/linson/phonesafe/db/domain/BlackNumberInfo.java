package com.linson.phonesafe.db.domain;

/**
 * Created by Administrator on 2016/10/10.
 */
public class BlackNumberInfo {
    public int id;
    public String phone;
    public String mode;

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
