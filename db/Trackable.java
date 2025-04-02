package db;
import java.time.LocalDate;
import java.util.Date;

public interface Trackable {
    void setCreationDate(LocalDate date);
    LocalDate getCreationDate();
    void setLastModificationDate(LocalDate date);
    LocalDate getLastModificationDate();
}
