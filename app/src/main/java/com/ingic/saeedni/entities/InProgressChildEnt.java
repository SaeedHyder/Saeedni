package com.ingic.saeedni.entities;



public class InProgressChildEnt {

    String txt_job;
    String Earning;
    String TotalEarning;

    public InProgressChildEnt(String txt_job, String Earning, String TotalEarning){

       setTxt_job(txt_job);
        setEarning(Earning);
        setTotalEarning(TotalEarning);
    }


    public String getTxt_job() {
        return txt_job;
    }

    public void setTxt_job(String txt_job) {
        this.txt_job = txt_job;
    }

    public String getEarning() {
        return Earning;
    }

    public void setEarning(String earning) {
        Earning = earning;
    }

    public String getTotalEarning() {
        return TotalEarning;
    }

    public void setTotalEarning(String totalEarning) {
        TotalEarning = totalEarning;
    }
}
