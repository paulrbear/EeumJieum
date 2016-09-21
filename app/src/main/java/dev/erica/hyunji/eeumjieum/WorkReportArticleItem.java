package dev.erica.hyunji.eeumjieum;

/**
 * Created by Erica on 2016-09-17.
 */
public class WorkReportArticleItem {

    int articleid;
    String objectroom, day, normalperson, outperson, hospitalperson, etcperson, programtxt;
    String[] normallist;
    String[] outlist;
    String[] hospitallist;
    String[] etclist;
    String[] programtxtlist;

    int normalcount, outcount, hospitalcount, etccount;


    public WorkReportArticleItem(int articleid, String objectroom, String day, String normalperson, String outperson, String hospitalperson, String etcperson, String programtxt){
        this.articleid = articleid;
        this.objectroom = objectroom;
        this.day = day;
        this.normalperson = normalperson;
        this.outperson = outperson;
        this.hospitalperson = hospitalperson;
        this.etcperson = etcperson;
        this.programtxt = programtxt;

        if(normalperson.equals("") || outperson.equals("") || hospitalperson.equals("") || etcperson.equals("")){

        }else {
            normallist = normalperson.split("/");
            normalcount = Integer.parseInt(normallist[0]);
            outlist = outperson.split("/");
            outcount = Integer.parseInt(outlist[0]);
            hospitallist = hospitalperson.split("/");
            hospitalcount = Integer.parseInt(hospitallist[0]);
            etclist = etcperson.split("/");
            etccount = Integer.parseInt(etclist[0]);
            programtxtlist = programtxt.split("/");
        }

    }


    public WorkReportArticleItem(String objectroom, String day, String normalperson, String outperson, String hospitalperson, String etcperson, String programtxt){

        this.objectroom = objectroom;
        this.day = day;
        this.normalperson = normalperson;
        this.outperson = outperson;
        this.hospitalperson = hospitalperson;
        this.etcperson = etcperson;
        this.programtxt = programtxt;

        if(normalperson.equals("") || outperson.equals("") || hospitalperson.equals("") || etcperson.equals("")){

        }else {
            normallist = normalperson.split("/");
            normalcount = Integer.parseInt(normallist[0]);
            outlist = outperson.split("/");
            outcount = Integer.parseInt(outlist[0]);
            hospitallist = hospitalperson.split("/");
            hospitalcount = Integer.parseInt(hospitallist[0]);
            etclist = etcperson.split("/");
            etccount = Integer.parseInt(etclist[0]);
            programtxtlist = programtxt.split("/");
        }

    }


    public int getNormalcount(){
        return normalcount;
    }
    public int getOutcount(){
        return outcount;
    }
    public int getHospitalcount(){
        return hospitalcount;
    }
    public int getEtccount(){
        return etccount;
    }


    public String getObjectroom(){return objectroom;}
    public String getDay(){return day;}
    public String[] getNormalList(){
        return normallist;
    }
    public String[] getOutList(){
        return  outlist;
    }
    public String[] getHospitallist(){
        return  hospitallist;
    }
    public String[] getEtclist(){
        return etclist;
    }
    public String[] getProgramtxtList(){
        return programtxtlist;
    }
    public int getArticleid(){return articleid;}





}
