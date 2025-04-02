package db.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException() {
        super("Error: Cannot find entity");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(int id) {
        super("Error: Cannot find entity with id " + id);
    }
}
