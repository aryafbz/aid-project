package todo.validator;

import db.*;
import db.exception.InvalidEntityException;
import todo.entity.Task;

public class TaskValidator implements Validator {
    @Override
    public void validate(Entity entity)throws InvalidEntityException{
        if(!(entity instanceof Task))
            throw new IllegalArgumentException("Cannot save task.\nError: Entity is not a task");
        else {
            if(((Task) entity).getTitle().isEmpty())
                throw new InvalidEntityException("Cannot save task.\nError: Title or description cannot be empty");
        }
    }
}
