import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
//サブメニュー○　ログアウト○　パスワード平文○ ハッシュ化　WEB

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Collator collator = Collator.getInstance(Locale.JAPANESE);

        List<User> Users = new ArrayList<>();


        try {
            BufferedReader userReader = new BufferedReader(new FileReader("users.txt"));

            String line;
            while ((line = userReader.readLine()) != null) {
                String[] data = line.split(",");
                String name = data[0];
                String password = data[1];

                Users.add(new User(name, password));
            }

            userReader.close();

        } catch (Exception e) {
            System.out.println("ユーザーファイルが見つかりません。初期ユーザーを作成します");
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("schedule.txt"));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                String name = data[0];
                String date = data[1];
                String title = data[2];

                Schedule schedule = new Schedule(date, title);

                for (User user : Users) {
                    if (user.getName().equals(name)) {
                        user.addSchedule(schedule);
                    }
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        User loginUser = null;

        if (Users.isEmpty()) {
            System.out.println("ユーザーが存在しません。最初のユーザーを作成してください。");
            System.out.println("ユーザー名を入力してください");
            String name = scanner.nextLine();

            System.out.println("パスワードを入力してください");
            String password = scanner.nextLine();
            Users.add(new User(name, password));
            System.out.println(name + "を作成しました");
        }
        boolean exitProgram = false;

        while (true) {
            while (true) {
                System.out.println("ログインするユーザーを選んでください");

                for (int i = 0; i < Users.size(); i++) {
                    System.out.println((i + 1) + ": " + Users.get(i).getName());
                }

                int loginChoice = scanner.nextInt();
                scanner.nextLine();

                if (loginChoice < 1 || loginChoice > Users.size()) {
                    System.out.println("正しい番号を入力してください");
                    continue;
                }

                User selectedUser = Users.get(loginChoice - 1);

                System.out.println("パスワードを入力してください");
                String inputPassword = scanner.nextLine();

                if (!selectedUser.getPassword().equals(inputPassword)) {
                    System.out.println("パスワードが違います");
                    continue;   // ログイン画面へ戻る
                }

                loginUser = selectedUser;   // ★ここで初めて代入
                System.out.println(loginUser.getName() + "でログインしました");
                break;
            }
            while (true) {

                System.out.println("==== メインメニュー ====");
                System.out.println("1: 予定操作");
                System.out.println("2: ユーザー管理");
                System.out.println("3: ログアウト");
                System.out.println("4: 終了");

                int mainChoice = scanner.nextInt();
                scanner.nextLine();

                if (mainChoice == 1) {
                    while (true) {

                        System.out.println("---- 予定操作 ----");
                        System.out.println("1: 予定を追加");
                        System.out.println("2: 予定を検索");
                        System.out.println("3: 予定を削除");
                        System.out.println("4: 予定を見る");
                        System.out.println("5: 戻る");

                        int subChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (subChoice == 1) {
                            System.out.println("日付を入力してください");
                            String date = scanner.nextLine();

                            System.out.println("内容を入力してください");
                            String title = scanner.nextLine();

                            Schedule schedule = new Schedule(date, title);
                            loginUser.addSchedule(schedule);

                            System.out.println("予定を追加しました");
                            System.out.println(loginUser.getName() + "の予定は" + schedule.getTitle() + "です");
                        } else if (subChoice == 2) {
                            System.out.println("検索する日付を入力してください");
                            String searchDate = scanner.nextLine();

                            loginUser.searchSchedule(searchDate);
                        } else if (subChoice == 3) {
                            System.out.println("削除する日付を入力してください");
                            String removeDate = scanner.nextLine();

                            loginUser.removeSchedule(removeDate);
                            System.out.println("該当する日付を削除しました");
                        } else if (subChoice == 4) {
                            System.out.println("=== " + loginUser.getName() + "の予定 ===");
                            loginUser.showSchedules();
                        } else if (subChoice == 5) {
                            break; // メインメニューに戻る
                        } else {
                            System.out.println("正しい番号を入力してください");
                        }
                    }
                } else if (mainChoice == 2) {
                    while (true) {
                        System.out.println("---- ユーザー管理 ----");
                        System.out.println("1: ユーザー追加");
                        System.out.println("2: ユーザー削除");
                        System.out.println("3: 戻る");

                        int userMenuChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (userMenuChoice == 1) {

                            System.out.println("追加するユーザー名を入力してください");
                            String name = scanner.nextLine();

                            if (name.isEmpty()) {
                                System.out.println("名前を入力してください");
                                continue;
                            }
                            boolean exists = false;
                            for (User user : Users) {
                                    if (user.getName().equals(name)) {
                                        exists = true;
                                        break;
                                    }
                            }
                                if (exists) {
                                    System.out.println("そのユーザーはすでに存在します");
                                } else {
                                    System.out.println("パスワードを入力してください");
                                    String password = scanner.nextLine();

                                    Users.add(new User(name, password));
                                    Users.sort((a, b) -> collator.compare(a.getName(), b.getName()));
                                    saveUsers(Users);

                                    System.out.println(name + "を追加しました");
                                }
                        } else if (userMenuChoice == 2) {
                            if (Users.isEmpty()) {
                                System.out.println("削除できるユーザーがいません");
                                continue;
                            }
                            System.out.println("削除するユーザーを選んでください");
                            for (int i = 0; i < Users.size(); i++) {
                                System.out.println((i + 1) + ": " + Users.get(i).getName());
                            }

                            int userChoice = scanner.nextInt();
                            scanner.nextLine();

                            if (userChoice < 1 || userChoice > Users.size()) {
                                System.out.println("正しい番号を入力してください");
                                continue;
                            }
                            User removedUser = Users.get(userChoice - 1);
                            Users.remove(userChoice - 1);

                            System.out.println(removedUser.getName() + "を削除しました");

                            try {
                                FileWriter userWriter = new FileWriter("users.txt");
                                for (User user : Users) {
                                    userWriter.write(user.getName() + "\n");
                                }

                                userWriter.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (userMenuChoice == 3) {

                            break; // メインへ戻る
                        } else {
                            System.out.println("正しい番号を入力してください");
                        }
                    }
                } else if (mainChoice == 3) {
                    saveUsers(Users);
                    System.out.println("ログアウトしました");
                    break; // ログイン画面に戻る
                } else if (mainChoice == 4) {
                    saveUsers(Users);
                    System.out.println("終了します");
                    exitProgram = true;
                    break;
                } else {
                    System.out.println("正しい番号を入力してください");
                }
            }
            if (exitProgram){
                break;
            }
            try {
                FileWriter writer = new FileWriter("schedule.txt");

                for (User user : Users) {
                    for (Schedule schedule : user.getList()) {
                        writer.write(user.getName() + "," + schedule.getDate() + "," + schedule.getTitle() + "\n");
                    }
                }
                writer.close();
                System.out.println("上記の作業を完了しました");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void saveUsers(List<User> users) {
        try {
            FileWriter writer = new FileWriter("users.txt");
            for (User user : users) {
                writer.write(user.getName() + "," + user.getPassword() +"\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void showAllSchedules (List < User > users) {
        System.out.println("========== 予定一覧 ==========");
        for (User user : users) {
            System.out.println(user.getName() + "の予定:");
            user.showSchedules();
            System.out.println();
        }
    }
    static void showOneSchedule(List<User> users, Scanner scanner) {
        System.out.println("誰の予定を見ますか？");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ":" + users.get(i).getName());
        }

        int userChoice = scanner.nextInt();
        scanner.nextLine();
        User selectedUser = users.get(userChoice - 1);

        System.out.println("====" + selectedUser.getName() + "の予定 ====");
        selectedUser.showSchedules();
    }
}