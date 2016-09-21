package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-15.
 */
public class RoomUserItem {
    private int uImg;
    private String name;

    public int getuImg() {return uImg; }
    public String getName(){ return name; }


    public RoomUserItem(String name, int uImg){
        this.uImg = uImg;
        this.name = name;

    }


}
