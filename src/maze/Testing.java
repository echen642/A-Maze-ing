package maze;

public class Testing {

    public static void main(String[] args) {
        MazeGenerator generator = new MazeGenerator();
        for (int i = 0; i < 100; i++) {
            generator.setMazeSize(20, 20);
        }
    }
        
}
