package todo.service;

import db.Database;
import db.Entity;
import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Task;
import todo.serializer.TaskSerializer;
import todo.validator.TaskValidator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class TaskService {
    static int addFlag = 0;

    public static void setAsCompleted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.COMPLETED);

        Database.update(task);
    }

    public static void setAsInProgress(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.IN_PROGRESS);

        Database.update(task);
    }

    public static void setAsNotStarted(int taskId) throws InvalidEntityException {
        Task task = (Task) Database.get(taskId);
        task.setStatus(Task.Status.NOT_STARTED);

        Database.update(task);
    }

    public static void addTask() {
        Scanner input = new Scanner(System.in);
        if (addFlag == 0) {
            Database.registerValidator(Task.TASK_ENTITY_CODE, new TaskValidator());
            addFlag++;
        }
        try{
            Task task = new Task();
            System.out.println("Title: ");
            String title = input.nextLine();
            task.setTitle(title);

            System.out.println("Description: ");
            String description = input.nextLine();
            task.setDescription(description);
            Database.add(task);

            System.out.println("Due date(yyyy-mm-dd): ");
            String dueDate = input.nextLine();
            task.setDueDate(LocalDate.parse(dueDate));
            Database.update(task);

            TaskService.setAsNotStarted(task.id);
            System.out.println("Task saved successfully.");
            System.out.println("ID: " + task.id);
            System.out.println("Creation date: " + task.getCreationDate());
        } catch (InvalidEntityException e) {
            System.out.println(e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
        }
    }

    public static void deleteTask() {
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();

        try {
            Database.delete(id);
            StepService.deleteStep(id);
            System.out.println("Entity with ID=" + id + " successfully deleted.");
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidEntityException e) {
            System.out.println("Cannot delete entity with ID=" + id + ".");
        }
    }

    public static void updateTask() {
        Scanner input = new Scanner(System.in);
        try{
            System.out.println("ID: ");
            int id = input.nextInt();
            input.nextLine();
            Entity entity = Database.get(id);
            Task task = (Task) entity;

            System.out.println("Field(title, description, due date(yyyy-mm-dd), status(NOT_STARTED, COMPLETED, IN_PROGRESS)): ");
            String field = input.nextLine().toLowerCase();

            System.out.println("New Value: ");
            String newValue = input.nextLine();

            String oldValue = "";
            int flag = 0;

            switch (field) {
                case "title":
                    oldValue = task.getTitle();
                    task.setTitle(newValue);
                    flag = 1;
                    break;
                case "description":
                    oldValue = task.getDescription();
                    task.setDescription(newValue);
                    flag = 1;
                    break;
                case "due date":
                    oldValue = task.getDueDate().toString();
                    task.setDueDate(LocalDate.parse(newValue));
                    flag = 1;
                    break;
                case "status":
                    oldValue = task.getStatus().toString();
                    task.setStatus(Task.Status.valueOf(newValue));
                    flag = 1;
                    break;
                default:
                    System.out.println("Invalid field.");
                    break;
            }

            if (flag == 1) {
                Database.update(task);
                System.out.println("Task updated successfully.");
                System.out.println("Field: " + field);
                System.out.println("Old Value: " + oldValue);
                System.out.println("New Value: " + newValue);
                System.out.println("Modification date: " + task.getLastModificationDate());

                if (task.getStatus() == Task.Status.COMPLETED) {
                    StepService.updateStep(task.id);
                }
            }
        } catch (NumberFormatException e) {
        System.out.println("Error: your entry is not valid for date field");
        } catch (InvalidEntityException e) {
        System.out.println(e.getMessage());
        } catch (EntityNotFoundException e) {
        System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        }
}

    public static void getTask() {
        Scanner input = new Scanner(System.in);

        System.out.println("ID: ");
        int id = input.nextInt();
        try {
            Entity e = Database.get(id);
            Task task = (Task) e;
            System.out.println("ID: " + task.id);
            System.out.println("Title: " + task.getTitle());
            System.out.println("Description: " + task.getDescription());
            System.out.println("Due date: " + task.getDueDate().toString());
            System.out.println("Status: " + task.getStatus());
            System.out.println("Steps: ");

            StepService.getStep(task.id);
        }
        catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllTasks() {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            System.out.println("ID: " + e.id);
            System.out.println("Title: " + e.getTitle());
            System.out.println("Description: " + e.getDescription());
            System.out.println("Due date: " + e.getDueDate());
            System.out.println("Status: " + e.getStatus());
            System.out.println("Steps: ");
            StepService.getStep(e.id);
            System.out.println();
        }
    }

    public static void getIncompleteTasks() {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getStatus() == Task.Status.IN_PROGRESS || e.getStatus() == Task.Status.NOT_STARTED){
                System.out.println("ID: " + e.id);
                System.out.println("Title: " + e.getTitle());
                System.out.println("Description: " + e.getDescription());
                System.out.println("Due date: " + e.getDueDate());
                System.out.println("Status: " + e.getStatus());
                System.out.println("Steps: ");
                StepService.getStep(e.id);
                System.out.println();
            }
        }
    }
    public static void getCompletedTasks() {
        ArrayList<Task> tasks = new  ArrayList<>();
        for(Entity e : Database.getAll(Task.TASK_ENTITY_CODE)){
            if(e instanceof Task){
                tasks.add((Task) e);
            }
        }
        tasks.sort(Comparator.comparing(Task::getDueDate));
        for(Task e : tasks){
            if(e.getStatus() == Task.Status.COMPLETED){
                System.out.println("ID: " + e.id);
                System.out.println("Title: " + e.getTitle());
                System.out.println("Description: " + e.getDescription());
                System.out.println("Due date: " + e.getDueDate());
                System.out.println("Status: " + e.getStatus());
                System.out.println("Steps: ");
                StepService.getStep(e.id);
                System.out.println();
            }
        }
    }
}


