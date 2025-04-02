import db.Database;
import todo.entity.Step;
import todo.entity.Task;
import todo.serializer.StepSerializer;
import todo.serializer.TaskSerializer;
import todo.service.StepService;
import todo.service.TaskService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome\nhere is the list of things you can do\n1.add task\n2.add step\n3.delete\n4.update task\n5.update step\n6.get task-by-id\n7.get all-tasks\n8.get incomplete-tasks\n9.get complete-tasks\n10.save\n11.load\n12.exit");
        System.out.println();
        String command;
        Database.registerSerializer(Task.TASK_ENTITY_CODE , new TaskSerializer());
        Database.registerSerializer(Step.STEP_ENTITY_CODE , new StepSerializer());
        while (true) {
            System.out.println("Enter your command: ");
            command = input.nextLine().toLowerCase();
            if(command.equals("exit")) {
                break;
            }
            switch (command) {
                case "add task":
                    TaskService.addTask();
                    break;
                case "add step":
                    StepService.addStep();
                    break;
                case "delete":
                    TaskService.deleteTask();
                    break;
                case "update task":
                    TaskService.updateTask();
                    break;
                case "update step":
                    StepService.updateStep();
                    break;
                case "get task-by-id":
                    TaskService.getTask();
                    break;
                case "get all-tasks":
                    TaskService.getAllTasks();
                    break;
                case "get incomplete-tasks":
                    TaskService.getIncompleteTasks();
                    break;
                case "get complete-tasks":
                    TaskService.getCompletedTasks();
                    break;
                case "save":
                    Database.save();
                    break;
                case "load":
                    Database.load();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}