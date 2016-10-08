package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-17.
 */
public class PersonalWorkReportItem {
    int id;
    int reportkey;
    String objectName;
    String objectRoom;
    int objectImg;
    int status;     // 0:normal 1:out   2:hospital  3:etc
    String objectContext;
    int meal1;
    int meal2;
    int meal3;


    PersonalWorkReportItem(int id, int reportkey, String objectName, String objectRoom, int objectImg, int status, String objectContext, int meal1, int meal2, int meal3){
        this.id = id;
        this.reportkey = reportkey;
        this.objectName = objectName;
        this.objectRoom = objectRoom;
        this.objectImg = objectImg;
        this.status = status;
        this.objectContext = objectContext;
        this.meal1 = meal1;
        this.meal2 = meal2;
        this.meal3 = meal3;
    }

    PersonalWorkReportItem(int reportkey, String objectName, String objectRoom, int objectImg, int status, String objectContext, int meal1, int meal2, int meal3){
        this.reportkey = reportkey;
        this.objectName = objectName;
        this.objectRoom = objectRoom;
        this.objectImg = objectImg;
        this.status = status;
        this.objectContext = objectContext;
        this.meal1 = meal1;
        this.meal2 = meal2;
        this.meal3 = meal3;
    }

    public int getObjectImg(){return objectImg;}
    public String getObjectName(){return objectName;}
    public String getObjectContext(){return objectContext;}
    public int getId(){return id;}
    public int getStatus(){return status;}
    public int getMeal1(){return meal1;}
    public int getMeal2(){return meal2;}
    public int getMeal3(){return meal3;}
    public int getReportkey(){return reportkey;}
    public String getObjectRoom(){return objectRoom;   }

}
