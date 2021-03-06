package it.polimi.ingsw;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.view.cli.CLI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;

/**
 * File resource loader
 */
public abstract class Resources {
    private static String godConfigPath = null;
    private static boolean customConfig = false;

    /**
     * Choose a file to load the god configuration from. The file should contain a valid JSON for a {@code List<God>}
     * @param path path to the configuration file
     */
    public static void setGodConfigFile(String path) {
        if (path != null) {
            godConfigPath = path;
            customConfig = true;
        } else {
            godConfigPath = null;
            customConfig = false;
        }
    }

    /**
     * @return true if a god configuration file was selected, false otherwise
     */
    public static boolean usingCustomConfig() {
        return customConfig;
    }

    /**
     * Get the god configuration file contents in string format.
     * @param context Context from which to load the resources.
     * @return String content of the file
     */
    public static String loadGodConfig(Object context) {
        InputStream config;
        if (customConfig) {
            try{
                config = new FileInputStream(godConfigPath);
            } catch (FileNotFoundException e) {
                CLI.error("File not found: \"" + godConfigPath + "\" falling back to default configuration");
                config = context.getClass().getClassLoader().getResourceAsStream("config/gods.json");
            }
        } else {
            config = context.getClass().getClassLoader().getResourceAsStream("config/gods.json");
        }


        if (config != null) {
            Scanner scanner = new Scanner(config);
            scanner.useDelimiter(""); // Read to end
            StringBuilder sb = new StringBuilder();
            while(scanner.hasNext()){
                sb.append(scanner.next());
            }
            return sb.toString();
        } else {
            CLI.error("ERROR: Could not load god configuration");
            return "";
        }
    }

    /**
     * Get an {@link ImageView} for the god with name {@code godName} if there is one.
     * @param godName Name of the god
     * @param context Context from which to load the resources.
     * @return Optional of {@link ImageView} if a corresponding image exists, an empty optional otherwise
     */
    public static Optional<ImageView> loadGodCard(Object context, String godName) {
        String id = godName.strip().toLowerCase();
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/card_" + id + ".png");
        if (stream != null) {
            Image img = new Image(stream);
            return Optional.of(new ImageView(img));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get a default {@link ImageView} for a generic god.
     * @param context Context from which to load the resources.
     * @return ImageView for a generic god
     */
    public static ImageView loadGodCard(Object context) {
        return loadImage(context, "drawable/card_default.png");
    }

    /**
     * @param context Context from which to load the resources.
     * @return ImageView for the board background
     */
    public static ImageView loadBoardBackground(Object context) {
        return loadImage(context, "drawable/bg_board_sea.png");
    }

    /**
     * @param context Context from which to load the resources.
     * @return ImageView for the board foreground
     */
    public static ImageView loadBoardForeground(Object context) {
        return loadImage(context, "drawable/bg_board_transparent.png");
    }

    /**
     * Get the {@link ImageView} representation of a {@link Building}
     * @param building Building to render
     * @param context Context from which to load the resources.
     * @return ImageView rendition of the building
     */
    public static ImageView loadBuilding(Object context, Building building) {
        if (building.hasDome()) {
            return loadImage(context, "drawable/cell_dome.png");
        } else {
            return switch (building.getLevel()) {
                case LEVEL0 -> new ImageView();
                case LEVEL1 -> loadImage(context, "drawable/cell_l1.png");
                case LEVEL2 -> loadImage(context, "drawable/cell_l2.png");
                case LEVEL3 -> loadImage(context, "drawable/cell_l3.png");
            };
        }
    }

    /**
     * Get an {@link Image} rendition of a pawn
     * @param id pawn color, use different id for different players
     * @param context Context from which to load the resources.
     * @return Optional of {@link Image} if an image corresponding to the id exists, an empty optional otherwise
     */
    public static Optional<Image> loadPawn(Object context, int id) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream("drawable/pawn_" + id + ".png");
        if (stream != null) {
            Image img = new Image(stream);
            return Optional.of(img);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Get an image to use as overlay on a cell
     * @param context Context from which to load the resources.
     * @return ImageView for a cell highlight, an empty ImageView if the resource does not exist
     */
    public static ImageView loadCellHighlight(Object context) {
        return loadImage(context, "drawable/highlight_0.png");
    }

    private static ImageView loadImage(Object context, String path) {
        InputStream stream = context.getClass().getClassLoader().getResourceAsStream(path);
        if (stream != null) {
            Image img = new Image(stream);
            return new ImageView(img);
        } else {
            return new ImageView();
        }
    }
}
