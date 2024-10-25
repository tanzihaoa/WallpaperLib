package com.tzh.wallpaperlib.dao.dto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class WidgetDto {
    @Id(autoincrement = true)
    public Long id;

    public String name;

    public String token;

    public String widget_id;
    public String text1;
    public String text2;
    public String text3;
    public String text4;
    public String text5;
    public String text6;
    public String text7;
    public String text8;
    public String text9;
    public String text10;
    public int num1;
    public int num2;
    public int num3;
    public int num4;
    public int num5;
    public int num6;
    public int num7;
    public int num8;
    public int num9;
    public int num10;

    @Generated(hash = 199081534)
    public WidgetDto(Long id, String name, String token, String widget_id,
            String text1, String text2, String text3, String text4, String text5,
            String text6, String text7, String text8, String text9, String text10,
            int num1, int num2, int num3, int num4, int num5, int num6, int num7,
            int num8, int num9, int num10) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.widget_id = widget_id;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.text5 = text5;
        this.text6 = text6;
        this.text7 = text7;
        this.text8 = text8;
        this.text9 = text9;
        this.text10 = text10;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
        this.num5 = num5;
        this.num6 = num6;
        this.num7 = num7;
        this.num8 = num8;
        this.num9 = num9;
        this.num10 = num10;
    }

    @Generated(hash = 1857988262)
    public WidgetDto() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText1() {
        return this.text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return this.text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getText3() {
        return this.text3;
    }

    public void setText3(String text3) {
        this.text3 = text3;
    }

    public String getText4() {
        return this.text4;
    }

    public void setText4(String text4) {
        this.text4 = text4;
    }

    public String getText5() {
        return this.text5;
    }

    public void setText5(String text5) {
        this.text5 = text5;
    }

    public String getText6() {
        return this.text6;
    }

    public void setText6(String text6) {
        this.text6 = text6;
    }

    public String getText7() {
        return this.text7;
    }

    public void setText7(String text7) {
        this.text7 = text7;
    }

    public String getText8() {
        return this.text8;
    }

    public void setText8(String text8) {
        this.text8 = text8;
    }

    public String getText9() {
        return this.text9;
    }

    public void setText9(String text9) {
        this.text9 = text9;
    }

    public String getText10() {
        return this.text10;
    }

    public void setText10(String text10) {
        this.text10 = text10;
    }

    public int getNum1() {
        return this.num1;
    }

    public void setNum1(int num1) {
        this.num1 = num1;
    }

    public int getNum2() {
        return this.num2;
    }

    public void setNum2(int num2) {
        this.num2 = num2;
    }

    public int getNum3() {
        return this.num3;
    }

    public void setNum3(int num3) {
        this.num3 = num3;
    }

    public int getNum4() {
        return this.num4;
    }

    public void setNum4(int num4) {
        this.num4 = num4;
    }

    public int getNum5() {
        return this.num5;
    }

    public void setNum5(int num5) {
        this.num5 = num5;
    }

    public int getNum6() {
        return this.num6;
    }

    public void setNum6(int num6) {
        this.num6 = num6;
    }

    public int getNum7() {
        return this.num7;
    }

    public void setNum7(int num7) {
        this.num7 = num7;
    }

    public int getNum8() {
        return this.num8;
    }

    public void setNum8(int num8) {
        this.num8 = num8;
    }

    public int getNum9() {
        return this.num9;
    }

    public void setNum9(int num9) {
        this.num9 = num9;
    }

    public int getNum10() {
        return this.num10;
    }

    public void setNum10(int num10) {
        this.num10 = num10;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWidget_id() {
        return this.widget_id;
    }

    public void setWidget_id(String widget_id) {
        this.widget_id = widget_id;
    }
}
