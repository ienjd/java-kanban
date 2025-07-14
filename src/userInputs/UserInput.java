package userInputs;

import java.util.Scanner;

public class UserInput {
    Scanner scanner = new Scanner(System.in);

    public String getUserInput() {
        String command = scanner.nextLine();
        return command;
    }

    public String getTitle() {
        System.out.println("Введите название задачи");
        String title = scanner.nextLine();
        return title;
    }

    public String getDescription() {
        System.out.println("Введите описание задачи");
        String description = scanner.nextLine();
        return description;
    }
}
