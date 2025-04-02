package todo.serializer;

import db.Serializer;
import db.*;
import todo.entity.Step;

import java.time.LocalDate;

public class StepSerializer implements Serializer {
    @Override
    public String serialize(Entity e){
        if(e instanceof Step){
            Step step = (Step)e;
            return "Step" + "/" + step.id + "/" + step.getTaskRef() + "/" + step.getTitle() + "/" + step.getStatus() + "/" + step.getCreationDate() + "/" + step.getLastModificationDate();
        }
        throw new IllegalArgumentException("You should provide a Step");
    }

    @Override
    public Entity deserialize(String s){
        String[] split = s.split("/");
        Step step = new Step();
        step.id = Integer.parseInt(split[1]);
        step.setTaskRef(Integer.parseInt(split[2]));
        step.setTitle(split[3]);
        String status = split[4];
        switch (status){
            case "NOT_STARTED":
                step.setStatus(Step.Status.NOT_STARTED);
                break;
            case "COMPLETED":
                step.setStatus(Step.Status.COMPLETED);
                break;
        }
        step.setCreationDate(LocalDate.parse(split[5]));
        step.setLastModificationDate(LocalDate.parse(split[6]));

        return step;
    }

}
