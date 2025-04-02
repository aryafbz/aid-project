package db;

import db.exception.EntityNotFoundException;
import db.exception.InvalidEntityException;
import todo.entity.Step;
import todo.entity.Task;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Database {
    private static ArrayList<Entity> entities = new ArrayList<>();
    private static int counter = 0;
    private static HashMap<Integer , Validator> validators = new HashMap<>();
    private static HashMap<Integer , Serializer> serializers = new HashMap<>();

    public static void add(Entity e) throws InvalidEntityException {
        Validator validator = validators.get(e.getEntityCode());
        if(e instanceof Trackable && ((Trackable) e).getCreationDate() == null && ((Trackable) e).getLastModificationDate() == null) {
            LocalDate today = LocalDate.now();
            ((Trackable) e).setCreationDate(today);
            ((Trackable) e).setLastModificationDate(today);
        }
        if (validator != null) {
            validator.validate(e);
        }

        e.id = ++counter;
        entities.add(e.copy());
    }

    public static Entity get(int id) {
        for (Entity e : entities) {
            if (e.id == id) {
                return e.copy();
            }
        }
        throw new EntityNotFoundException(id);
    }

    public static void delete(int id) throws EntityNotFoundException {
        int flag = 0;
        for (Entity e : entities) {
            if (e.id == id) {
                entities.remove(e);
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            throw new EntityNotFoundException(id);
        }

    }

    public static void update(Entity e) throws InvalidEntityException {
        int flag = 0;
        Validator validator = validators.get(e.getEntityCode());
        if(validator != null) {
            validator.validate(e);
        }

        if (e instanceof Trackable) {
            ((Trackable) e).setLastModificationDate(LocalDate.now());
        }

        for (Entity e1 : entities) {
            if (e1.id == e.id) {
                entities.set(entities.indexOf(e1), e.copy());
                flag = 1;
            }
        }
        if (flag == 0) {
            throw new EntityNotFoundException(e.id);
        }
    }

    public static void registerValidator(int entityCode , Validator validator) {
        if(validators.containsKey(entityCode)) {
            throw new IllegalArgumentException("Entity with entity code " + entityCode + " already exists");
        }
        else {
            validators.put(entityCode, validator);
        }
    }

    public static void registerSerializer(int entityCode , Serializer serializer) {
        if(serializers.containsKey(entityCode)) {
            throw new IllegalArgumentException("Entity with entity code " + entityCode + " already exists");
        }
        else {
            serializers.put(entityCode, serializer);
        }
    }

    public static ArrayList<Entity> getAll(int entityCode) {
        ArrayList<Entity> result = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getEntityCode() == entityCode) {
                result.add(e.copy());
            }
        }
        return result;
    }

    public static void save(){

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("db.txt"));
            for (Entity e : entities) {
                Serializer serializer = serializers.get(e.getEntityCode());
                writer.write(serializer.serialize(e));
                writer.newLine();
            }
            writer.close();
            System.out.println("Saved successfully");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void load(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("db.txt"));
            String line;
            while ((line = reader.readLine()) != null){
                if(line.contains("Task")) {
                    Serializer serializer = serializers.get(Task.TASK_ENTITY_CODE);
                    try {
                        Database.add(serializer.deserialize(line));
                    } catch (InvalidEntityException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if(line.contains("Step")) {
                    Serializer serializer = serializers.get(Step.STEP_ENTITY_CODE);
                    try{
                        Database.add(serializer.deserialize(line));
                    }
                    catch (InvalidEntityException e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            reader.close();
            System.out.println("Loaded successfully");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
