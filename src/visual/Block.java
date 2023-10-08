package visual;
import edu.macalester.graphics.Rectangle;
import java.awt.Color;
import java.util.Map;
/*
 * Represents a single block in the grid
 */
public class Block extends Rectangle {
    // Public definer for what each symbol means
    public static final Map<Integer, Color> colorCode = Map.of(
        0, new Color(0, 0, 0), // Wall
        1, new Color(255, 255, 255), // Path
        2, new Color(148, 0, 211), // Start
        3, new Color(0, 255, 0), // End
        4, new Color(255, 0, 0), // Potential Path
        5, new Color(0, 0, 255) // Solution Path
    );

    public static final Map<Integer, String> colorWord = Map.of(
        0, "Wall",
        1, "Path",
        2, "Start",
        3, "End",
        4, "Potential Path",
        5, "Solution"
    );

    public Block(double x, double y, double width, double height) {
        super(x, y, width, height);
        setStrokeColor(colorCode.get(0));
        setFillColor(colorCode.get(0));
    }

    /*
     * Sets color of rectangle based on hashmap
     */
    public void setFill(int color) {
        setStrokeColor(colorCode.get(color));
        setFillColor(colorCode.get(color));
    }
    
}
