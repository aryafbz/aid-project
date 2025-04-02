package todo.validator;
import db.*;
import db.exception.InvalidEntityException;
import todo.entity.Step;

public class StepValidator implements Validator {
    @Override
    public void validate(Entity entity)throws InvalidEntityException{
        if(!(entity instanceof Step))
            throw new IllegalArgumentException("Entity must be a Step");
        else{
            if(((Step) entity).getTitle().isEmpty()){
                throw new InvalidEntityException("Cannot save.\nError: Title cannot be empty");
            }
            if((Database.get(((Step) entity).getTaskRef()) == null)) {
                throw new InvalidEntityException("Cannot save.\nError: can not find task with ID = " + ((Step) entity).getTaskRef());
            }
        }
    }
}
