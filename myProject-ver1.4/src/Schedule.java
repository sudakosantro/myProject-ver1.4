import java.util.ArrayList;
import java.util.List;
//最大人数４人で使えるプログラム追加機能に関しては可変式プログラムに記載

public class Schedule {
    private String date;
    private String title;

    Schedule(String date, String title){
        this.date = date;
        this.title = title;
    }

    String getDate(){
        return this.date;
    }
    String getTitle(){
        return this.title;
    }
    void setDate(String date){
        this.date = date;
    }
    void setTitle(String title){
        this.title = title;
    }

    void showSchedule(){
        System.out.println(date+"の予定は"+title+"です。");
    }
}