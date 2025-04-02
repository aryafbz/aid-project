package todo.entity;
import db.*;

import java.time.LocalDate;
import java.util.*;

public class Step extends Entity implements Trackable {
    public enum Status {
        NOT_STARTED, COMPLETED
    }

    private String title;
    private Status status;
    private int taskRef;
    private LocalDate creationDate;
    private LocalDate lastModificationDate;

    public static final int STEP_ENTITY_CODE = 5;

    @Override
    public Step copy(){
        Step copyStep = new Step();
        copyStep.id = this.id;
        copyStep.title = this.title;
        copyStep.status = this.status;
        copyStep.taskRef = this.taskRef;
        copyStep.creationDate = this.creationDate;
        copyStep.lastModificationDate = this.lastModificationDate;

        return copyStep;
    }

    @Override
    public int getEntityCode() {
        return STEP_ENTITY_CODE;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setTaskRef(int taskRef) {
        this.taskRef = taskRef;
    }

    public int getTaskRef() {
        return taskRef;
    }

    @Override
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setLastModificationDate(LocalDate lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public LocalDate getLastModificationDate() {
        return lastModificationDate;
    }
}
