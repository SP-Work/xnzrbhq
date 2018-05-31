package com.otitan.xnbhq.listviewinedittxt;

import android.os.Parcel;
import android.os.Parcelable;

import com.esri.core.map.CodedValueDomain;

/**
 * Created by aspsine on 15/10/11.
 *
 * lsitview item 自定义实体类
 */
public class Line implements Parcelable {
    /** 序号 */
    private int num;
    /** edittext显示的值 */
    private String text;
    /** textview显示的字段别名 */
    private String tview;
    /** edittext保存的值  对应字段的key */
    private String key;
    /** edittext保存的值 */
    private String value;
    /** edittext 焦点 */
    private boolean focus;
    /** 字段约束长度 */
    private int fLength;
    /**字段对应的domain*/
    private CodedValueDomain domain;
    /**字段数据类型*/
    private int fieldType;
    /**字段是否可以为*/
    private boolean isNullable;

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public int getFieldType() {
        return fieldType;
    }

    public void setFieldType(int fieldType) {
        this.fieldType = fieldType;
    }

    public CodedValueDomain getDomain() {
        return domain;
    }

    public void setDomain(CodedValueDomain domain) {
        this.domain = domain;
    }

    public Line() {
    }

    protected Line(Parcel in) {
        num = in.readInt();
        text = in.readString();
        tview = in.readString();
        key = in.readString();
        value = in.readString();
        focus = in.readByte() != 0;
        fLength = in.readInt();
        fieldType = in.readInt();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getTview() {
        return tview;
    }

    public void setTview(String tview) {
        this.tview = tview;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getfLength() {
        return fLength;
    }

    public void setfLength(int fLength) {
        this.fLength = fLength;
    }



    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(num);
        dest.writeString(value);
        dest.writeString(text);
        dest.writeString(tview);
        dest.writeString(key);
        dest.writeByte((byte) (focus ? 1 : 0));
        dest.writeInt(fLength);
        dest.writeInt(fieldType);
    }
}
