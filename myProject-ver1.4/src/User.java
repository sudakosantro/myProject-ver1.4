import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private List<Schedule> list;

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.list = new ArrayList<>();
    }

    public  String getName(){
        return  name;
}

    public  String getPassword(){
        return password;
    }
    public List<Schedule> getList(){
        return list;
    }



    void addSchedule(Schedule schedule) {
        list.add(schedule);

        list.sort((a, b) -> a.getDate().compareTo(b.getDate()));
    }
    void showSchedules() {
        if (list.isEmpty()) {
            System.out.println("予定なし");
        } else {
            for (Schedule schedule : list) {
                schedule.showSchedule();
            }
        }
    }
    void removeSchedule(String date) {
        for (Schedule schedule : list) {
            if (schedule.getDate().equals(date)) {
                list.remove(schedule);
                break;
            }
        }
    }

    void searchSchedule(String date){
        boolean found = false;
        for (Schedule schedule : list) {
            if (schedule.getDate().equals(date)){
                System.out.println(this.getName() + "の"
                        + schedule.getDate() + "の予定は" + schedule.getTitle() + "です。");
                found = true;
            }
        }
        if (!found){
            System.out.println("見つかりません");
        }
    }
}