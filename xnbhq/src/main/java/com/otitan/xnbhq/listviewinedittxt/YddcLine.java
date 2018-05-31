package com.otitan.xnbhq.listviewinedittxt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * item实体类（舍去）
 */
public class YddcLine extends Line implements Parcelable {
	
	private int num;
	private String text;
    private String alias;
    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private String key;
    private String type;
    private String mbfield;
    private String isedit;
	private boolean focus;
	
	public YddcLine() {
    }

	protected YddcLine(Parcel in) {
        num = in.readInt();
        text = in.readString();
        alias = in.readString();
        key = in.readString();
        type = in.readString();
        mbfield = in.readString();
        isedit = in.readString();
        focus = in.readByte() != 0;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMbfield() {
		return mbfield;
	}

	public void setMbfield(String mbfield) {
		this.mbfield = mbfield;
	}

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		this.focus = focus;
	}
	
	public static final Creator<YddcLine> CREATOR = new Creator<YddcLine>() {
        @Override
        public YddcLine createFromParcel(Parcel in) {
            return new YddcLine(in);
        }

        @Override
        public YddcLine[] newArray(int size) {
            return new YddcLine[size];
        }
    };

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(num);
        dest.writeString(key);
        dest.writeString(text);
        dest.writeString(alias);
        dest.writeString(type);
        dest.writeString(mbfield);
        dest.writeString(isedit);
        dest.writeByte((byte) (focus ? 1 : 0));
	}

}
