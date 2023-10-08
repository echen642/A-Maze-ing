package visual;

import maze.MazeGenerator;

/*
 * We wanted to check the ratio between the grid size and how many updates were required to complete the maze. That way, we could come up with a more accurate estimate of how many times to call update per frame to get a rough runtime when showcasing the code
 */
public class Testing {
    private static final int NUMBER_OF_TESTS = 100;
    private static int grid_size_x = 1000;
    private static int grid_size_y = 1000;
    public static void main(String[] args) {
        MazeGenerator generator = new MazeGenerator();
        int sum = 0;
        for (int i = 0; i < NUMBER_OF_TESTS; i++) {
            generator.setMazeSize(grid_size_x, grid_size_y);
            while (!generator.update().isEmpty()) {
                sum++;
            }
        }
        double average_iterations_per_test = sum / NUMBER_OF_TESTS;
        double ratio = average_iterations_per_test / (grid_size_x * grid_size_y);
        System.out.println(ratio);

        // 20x20 = 1.1975 ratio
        // 40x40 = 1.2125 ratio
        // 100x100 = 1.2185 ratio
        // 1000x1000 = 1.221552 ratio
        
    }
}
