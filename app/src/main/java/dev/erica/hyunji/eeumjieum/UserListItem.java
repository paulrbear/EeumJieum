package dev.erica.hyunji.eeumjieum;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erica on 2016-09-07.
 */
public class UserListItem implements Parcelable{
    private int uImg;
    private String name;
    private String room;

    public int getuImg() {return uImg; }
    public String getName(){ return name; }
    public String getRoom() {return room;}


    public UserListItem(int uImg, String name, String room){
        this.uImg = uImg;
        this.name = name;
        this.room = room;
    }

    public UserListItem(Parcel in){
        this.uImg = in.readInt();
        this.name = in.readString();
        this.room = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uImg);
        dest.writeString(name);
        dest.writeString(room);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public Object createFromParcel(Parcel source) {
            return new UserListItem(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new UserListItem[size];
        }
    };

}
