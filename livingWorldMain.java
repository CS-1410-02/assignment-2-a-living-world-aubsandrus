import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Creature{
    private String name;
    private int x;
    private int y;
    private double chanceOfReplicating;
    private double chanceOfDying;
    private int stepsWithoutFood;

    public Creature(String name, int age, double chanceOfReplicating, double chanceOfDying, int x, int y){
        this.name = name;
        this.chanceOfReplicating = chanceOfReplicating;
        this.chanceOfDying = chanceOfDying;
        this.x = x;
        this.y = y;
        this.stepsWithoutFood = 0;
    }

    public void die(){
        System.out.println(name + " has died.");
    }

    public Creature reproduce(){
        if (Math.random() < chanceOfReplicating) {
            System.out.println(name + " has reproduced.");
            return new Creature(generateRandomName(), 0, chanceOfReplicating, chanceOfDying, x, y);
        }
        return null;
    }

    public void move(){
        Random random = new Random();
        int direction = random.nextInt(4);

        switch(direction){
            case 0:
                y--;
                break;
            case 1:
                y++;
                break;
            case 2:
                x--;
                break;
            case 3:
                x++;
                break;
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void incrementStepsWithoutFood(){
        stepsWithoutFood++;
    }

    public void resetStepsWithoutFood(){
        stepsWithoutFood = 0;
    }

    public int getStepsWithoutFood(){
        return stepsWithoutFood;
    }

    private static List<String> readRandomName(String filename, int numberOfNames){
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            ArrayList<String> allNames = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null){
                allNames.add(line.trim());
            }

            Collections.shuffle(allNames);

            return allNames.subList(0, Math.min(numberOfNames, allNames.size()));
        } catch (IOException e){
            e.printStackTrace();
            return Collections.singletonList("Eric");
        }
    }

        private static String generateRandomName(){
        List<String> names = readRandomName("name.txt", 5);
        return names.get(new Random().nextInt(names.size()));
    }

static class World{
    private Creature[][] environment;
    private ArrayList<Creature> creatures = new ArrayList<>();
    private double chanceOfSpawningNew;
    private double chanceOfSpawningFood;

    public World(int rows, int cols, double chanceOfSpawningNew, double chanceOfSpawningFood){
        this.chanceOfSpawningNew = chanceOfSpawningNew;
        this.chanceOfSpawningFood = chanceOfSpawningFood;
        this.environment = new Creature[rows][cols];
    }

    public void createCreature(){
        String name = generateRandomName();
        Creature creature = new Creature(name, 0, 0.5, 0.1, getRandomX(), getRandomY());
        creatures.add(creature);
        environment[creature.getY()][creature.getX()] = creature;
        System.out.println(name + " has been created at position (" + creature.getX() + ", " + creature.getY() + ").");
    }

    public void spawnFood(){
        if (Math.random() < chanceOfSpawningFood){
            int x = getRandomX();
            int y = getRandomY();
            environment[y][x] = null;
            System.out.println("Food has spawned at position (" + x + ", " + y + ").");
        }
    }

    public void simulate(int numIterations){
        for (int i = 0; i < numIterations; i++){
            System.out.println("\nIteration " + (i + 1));
            createCreature();
            spawnFood();

            for (Creature creature : new ArrayList<>(creatures)){
                creature.move();
                if (environment[creature.getY()][creature.getX()] != null){
                    Creature otherCreature = environment[creature.getY()][creature.getX()];
                    if (otherCreature != creature){
                        Creature newCreature = creature.reproduce();
                        if (newCreature != null){
                            creatures.add(newCreature);
                            environment[newCreature.getY()][newCreature.getX()] = newCreature;
                        }
                    }
                }
                
                if (Math.random() < creature.chanceOfDying){
                    creatures.remove(creature);
                    environment[creature.getY()][creature.getX()] = null;
                    creature.die();
                }

                if (Math.random() < creature.chanceOfDying || creature.getStepsWithoutFood() >= 3) {
                    creatures.remove(creature);
                    environment[creature.getY()][creature.getX()] = null;
                    creature.die();
                }

                Creature newCreature = creature.reproduce();
                if (newCreature != null){
                    creatures.add(newCreature);
                    environment[newCreature.getY()][newCreature.getX()] = newCreature;
                }
                creature.incrementStepsWithoutFood();
            }
        }
    }

    private int getRandomX(){
        return new Random().nextInt(environment[0].length);
    }

    private int getRandomY(){
        return new Random().nextInt(environment.length);
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
public static class Main {
        public static void main(String[] args){
            World world = new World(5, 5, 0.2, 0.3);
            world.simulate(5);
        }
    }

}


