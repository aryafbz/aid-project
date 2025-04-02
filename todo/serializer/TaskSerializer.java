package todo.serializer;

import db.Entity;
import db.Serializer;
import todo.entity.Task;
import java.time.LocalDate;
import java.util.Date;

public class TaskSerializer implements Serializer {
    @Override
    public String serialize(Entity e){
        if(e instanceof Task){
            Task task = (Task) e;
            return "Task" + "/" + task.id + "/" + task.getTitle() + "/" + task.getDescription() + "/" + task.getDueDate() + "/" + task.getStatus() + "/" + task.getCreationDate() + "/" + task.getLastModificationDate();
        }
        throw new IllegalArgumentException("You should provide a Task");
    }

    @Override
    public Entity deserialize(String s){
            String[] split = s.split("/");
            Task task = new Task();
            task.id = Integer.parseInt(split[1]);
            task.setTitle(split[2]);
            task.setDescription(split[3]);
            task.setDueDate(LocalDate.parse(split[4]));
            switch(split[5]){
                case"NOT_STARTED":
                    task.setStatus(Task.Status.NOT_STARTED);
                    break;
                case "COMPLETED":
                    task.setStatus(Task.Status.COMPLETED);
                    break;
                case "IN_PROGRESS":
                    task.setStatus(Task.Status.IN_PROGRESS);
                    break;
            }
            task.setCreationDate(LocalDate.parse(split[6]));
            task.setLastModificationDate(LocalDate.parse(split[7]));
            return task;
    }
}
