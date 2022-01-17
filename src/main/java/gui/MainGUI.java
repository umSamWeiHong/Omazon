package main.java.gui;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import main.java.gui.Controller.Controller;
import main.java.gui.Controller.MenuBarController;
import main.java.gui.Controller.SlideMenuController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.EnumMap;

public class MainGUI extends Application {

    private static Stage stage;
    private static final EnumMap<Page, FXMLLoader> sceneMap;
    private static final FXMLLoader menuBarLoader, slideMenuLoader;

    static {
        menuBarLoader = getLoaderWithNode("MenuBar");
        slideMenuLoader = getLoaderWithNode("SlideMenu");

        @SuppressWarnings("ConstantConditions")
        MenuBarController menuBarController = menuBarLoader.getController();
        @SuppressWarnings("ConstantConditions")
        SlideMenuController slideMenuController = slideMenuLoader.getController();

        sceneMap = new EnumMap<>(Page.class);
        for (Page page : Page.values())
            sceneMap.put(page, getLoaderWithNode(page.getFilename()));

        menuBarController.setSlideMenuController(slideMenuController);
    }

    public static FXMLLoader getMenuBarLoader() {
        return menuBarLoader;
    }

    public static FXMLLoader getSlideMenuLoader() {
        return slideMenuLoader;
    }

    public static FXMLLoader getLoaderWithNode(String filename) {
        try {
            FXMLLoader loader = new FXMLLoader(new File(
                    "src/main/resources/layout/" + filename + ".fxml").toURI().toURL());
            Parent root = loader.load();
            // Add the respective css file if it exists.
            if (new File("src/main/resources/css/" + filename + ".css").exists())
                root.getStylesheets().add(new File("src/main/resources/css/" + filename + ".css").toURI().toString());
            return loader;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void initializeStage() {
        stage.setTitle("Omazon");
        stage.getIcons().add(new Image("main/resources/img/icon.png"));
        stage.setScene(new Scene(new Group()));
        stage.show();
    }

    public static void loadScene(Page page) {
        double width = stage.getWidth();
        double height = stage.getHeight();

        FXMLLoader loader = sceneMap.get(page);
        stage.getScene().setRoot(loader.getRoot());
        ((Controller) loader.getController()).update();
        stage.setWidth(width);
        stage.setHeight(height);
    }

    @SuppressWarnings("ConstantConditions")
    public static void loadAllFonts() {
        File directory = new File("src/main/resources/font/");
        for (File file : directory.listFiles())
            Font.loadFont(file.toURI().toString(), 12);
    }

    /** Method to resize image */
    public static void resize(String inputImagePath, String outputImagePath) throws IOException {
        // Reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // Creates output image
        BufferedImage outputImage = new BufferedImage(300,
                300, inputImage.getType());

        // Scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, 300, 300, null);
        g2d.dispose();

        // Extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // Writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /** Method to convert image into Base64 string. */
    public static String encode(File img) {
        if (img != null) {
            try {
                FileInputStream input = new FileInputStream(img);
                byte[] bytes = new byte[(int) img.length()];
                input.read(bytes);
                return new String(Base64.getEncoder().encode(bytes));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Method to convert Base64 string into JavaFX image. */
    public static Image decode(String base64) {
        try {
            byte[] imageByte = Base64.getDecoder().decode(base64);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            BufferedImage image = ImageIO.read(bis);
            bis.close();
            if (image == null)
                return null;
            return SwingFXUtils.toFXImage(image, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void start(Stage stage) {
        MainGUI.stage = stage;
        MainGUI.loadAllFonts();
        MainGUI.initializeStage();
        MainGUI.loadScene(Page.HOME);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }
}
