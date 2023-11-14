import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Creature{
    private String name;
    private int age;
    private double chanceOfReplicating;
    private double chanceOfDying;

    public Creature(String name, int age, double chanceOfReplicating, double chanceOfDying){
        this.name = name;
        this.age = age;
        this.chanceOfReplicating = chanceOfReplicating;
        this.chanceOfDying = chanceOfDying;
    }

    public void die(){
        System.out.println(name + " has died.");
    }

    public Creature reproduce(){
        if (Math.random() < chanceOfReplicating) {
            System.out.println(name + " has reproduced.");
            return new Creature(generateRandomName(), 0, chanceOfReplicating, chanceOfDying);
        }
        return null;
    }

    private static String generateRandomName(){
        return readRandomName("name.txt");
    }

    private static String readRandomName(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            ArrayList<String> names = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null){
                names.add(line.trim());
            }
            return names.get(new Random().nextInt(names.size()));
        } catch (IOException e){
            e.printStackTrace();
            return "Eric";
        }
    }
static class World{
    private ArrayList<Creature> creatures = new ArrayList<>();
    private double chanceOfSpawningNew;
    private double chanceOfSpawningFood;

    public World(double chanceOfSpawningNew, double chanceOfSpawningFood){
        this.chanceOfSpawningNew = chanceOfSpawningNew;
        this.chanceOfSpawningFood = chanceOfSpawningFood;
    }

    public void createCreature(){
        String name = generateRandomName();
        Creature creature = new Creature(name, 0, 0.5, 0.1);
        creatures.add(creature);
        System.out.println(name + " has been created.");
    }

    public void spawnFood(){
        if (Math.random() < chanceOfSpawningFood){
            System.out.println("Food has spawned.");
        }
    }

    public void simulate(int numIterations){
        for (int i = 0; i < numIterations; i++){
            System.out.println("\nIteration " + (i + 1));
            createCreature();
            spawnFood();

            for (Creature creature : new ArrayList<>(creatures)){
                creature.age++;
                
                if (Math.random() < creature.chanceOfDying){
                    creatures.remove(creature);
                    creature.die();
                }

                Creature newCreature = creature.reproduce();
                if (newCreature != null){
                    creatures.add(newCreature);
                }
            }
        }
    }

    private String generateRandomName(){
        return readRandomName("name.txt");
    }

    private String readRandomName(String filename){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            ArrayList<String> names = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null){
                names.add(line.trim());
            }
            return names.get(new Random().nextInt(names.size()));
        } catch (IOException e){
            e.printStackTrace();
            return "Eric";
        }
    }

}
public class Main {
        public static void main(String[] args){
            World world = new World(0.2, 0.3);
            world.simulate(5);
        }
    }

}


