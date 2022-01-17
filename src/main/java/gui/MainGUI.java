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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.EnumMap;

public class MainGUI extends Application {

    public static Stage stage;
    private static EnumMap<Page, FXMLLoader> sceneMap;
    private static final FXMLLoader menuBarLoader, slideMenuLoader;

    static {
        menuBarLoader = getLoaderWithNode("MenuBar");
        slideMenuLoader = getLoaderWithNode("SlideMenu");

        @SuppressWarnings("ConstantConditions")
        MenuBarController menuBarController = menuBarLoader.getController();
        @SuppressWarnings("ConstantConditions")
        SlideMenuController slideMenuController = slideMenuLoader.getController();

        sceneMap = new EnumMap<>(Page.class);
        sceneMap.put(Page.LOGIN, getLoaderWithNode(Page.LOGIN.getFilename()));
        sceneMap.put(Page.REGISTER, getLoaderWithNode(Page.REGISTER.getFilename()));
        sceneMap.put(Page.HOME, getLoaderWithNode(Page.HOME.getFilename()));
        sceneMap.put(Page.PROFILE, getLoaderWithNode(Page.PROFILE.getFilename()));
        sceneMap.put(Page.STORE, getLoaderWithNode(Page.STORE.getFilename()));
        sceneMap.put(Page.CART, getLoaderWithNode(Page.CART.getFilename()));
        sceneMap.put(Page.ORDER, getLoaderWithNode(Page.ORDER.getFilename()));
        sceneMap.put(Page.FAVOURITE, getLoaderWithNode(Page.FAVOURITE.getFilename()));
        sceneMap.put(Page.SETTINGS, getLoaderWithNode(Page.SETTINGS.getFilename()));
        sceneMap.put(Page.PRODUCT, getLoaderWithNode(Page.PRODUCT.getFilename()));

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
}
