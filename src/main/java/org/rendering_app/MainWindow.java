package org.rendering_app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.rendering_app.render.Camera;

public class MainWindow extends Application {

    private final ListView<String> modelsList = new ListView<>();
    private final ListView<String> camerasList = new ListView<>();
    private final ListView<String> lightsList = new ListView<>();
    private final CheckBox showMeshCheckBox = new CheckBox("Show Mesh");
    private final CheckBox showTextureCheckBox = new CheckBox("Show Texture");
    private final CheckBox showIlluminationCheckBox = new CheckBox("Show Illumination");
    private Canvas renderCanvas;
    private Camera camera;

    @Override
    public void start(Stage stage) {
        camera = new Camera(
                new org.rendering_app.math.Vector3D(0, 0, 100),
                new org.rendering_app.math.Vector3D(0, 0, 0),
                1.0f, 1.0f, 0.01f, 1000.0f
        );

        BorderPane root = new BorderPane();
        MenuBar menuBar = createMenuBar(stage);
        VBox top = new VBox(menuBar, createCenterArea());
        root.setTop(top);
        root.setBottom(createStatusBar());

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("3D Model Renderer");
        stage.setScene(scene);
        stage.show();

        setupListeners();
        setupAnimationTimer();

        addCameraToList("0", "0", "100");
        camerasList.getSelectionModel().select(0);
        onUseCamera();
    }


    private MenuBar createMenuBar(Stage stage) {
        MenuBar bar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem loadModel = new MenuItem("Load Model...");
        loadModel.setOnAction(e -> onLoadModel());
        MenuItem saveModel = new MenuItem("Save Model...");
        saveModel.setOnAction(e -> onSaveModel());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> stage.close());
        file.getItems().addAll(loadModel, saveModel, new SeparatorMenuItem(), exit);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(e -> showInfoDialog("3D Model Renderer\nJavaFX"));
        help.getItems().add(about);

        bar.getMenus().addAll(file, help);
        return bar;
    }


    private Pane createCenterArea() {
        HBox center = new HBox(10);
        center.setPadding(new Insets(10));
        center.getChildren().addAll(
                createLeftPanel(),
                createCenterPanel(),
                createRightPanel()
        );
        HBox.setHgrow(center.getChildren().get(1), Priority.ALWAYS);
        return center;
    }

    private Pane createLeftPanel() {
        VBox left = new VBox(5);
        left.setPrefWidth(240);
        left.setPadding(new Insets(5));
        left.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-insets: 3;");
        Label title = new Label("Scene");
        title.setStyle("-fx-font-weight: bold;");

        Label modelsLabel = new Label("Models");
        modelsList.setPrefHeight(160);

        Button addModel = new Button("+");
        addModel.setOnAction(e -> onLoadModel());
        Button removeModel = new Button("-");
        removeModel.setOnAction(e -> onRemoveModel());
        HBox modelBtns = new HBox(5, addModel, removeModel);

        Label camerasLabel = new Label("Cameras");
        camerasList.setPrefHeight(120);
        Button useCam = new Button("Use");
        useCam.setOnAction(e -> onUseCamera());
        Button addCam = new Button("+");
        addCam.setOnAction(e -> onAddCamera());
        Button remCam = new Button("-");
        remCam.setOnAction(e -> onRemoveCamera());
        HBox camBtns = new HBox(5, useCam, addCam, remCam);

        left.getChildren().addAll(
                title,
                modelsLabel, modelsList, modelBtns,
                new Separator(Orientation.HORIZONTAL),
                camerasLabel, camerasList, camBtns
        );
        return left;
    }

    private Pane createCenterPanel() {
        VBox box = new VBox(5);
        box.setPrefWidth(800);
        box.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-insets: 3;");
        box.setPadding(new Insets(5));

        Label title = new Label("Viewport");
        title.setStyle("-fx-font-weight: bold;");

        renderCanvas = new Canvas(800, 600);
        StackPane canvasHolder = new StackPane(renderCanvas);
        VBox.setVgrow(canvasHolder, Priority.ALWAYS);

        box.getChildren().addAll(title, canvasHolder);
        return box;
    }

    private Pane createRightPanel() {
        VBox right = new VBox(5);
        right.setPrefWidth(260);
        right.setPadding(new Insets(5));
        right.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 3; -fx-border-insets: 3;");
        Label title = new Label("Controls");
        title.setStyle("-fx-font-weight: bold;");

        Button resetBtn = new Button("Reset");
        resetBtn.setOnAction(e -> onResetModel());
        Button rotateBtn = new Button("Rotate");
        rotateBtn.setOnAction(e -> onRotate());
        Button scaleBtn = new Button("Scale");
        scaleBtn.setOnAction(e -> onScale());
        Button translateBtn = new Button("Translate");
        translateBtn.setOnAction(e -> onTranslate());
        Button colorBtn = new Button("Select Color");
        colorBtn.setOnAction(e -> onSelectColor());

        showMeshCheckBox.setOnAction(e -> onShowMeshChanged());
        showTextureCheckBox.setOnAction(e -> onShowTextureChanged());
        showIlluminationCheckBox.setOnAction(e -> onShowIlluminationChanged());

        Button addTextureBtn = new Button("Add Texture");
        addTextureBtn.setOnAction(e -> onAddTexture());

        Label lightsLabel = new Label("Light sources");
        Button addLight = new Button("+");
        addLight.setOnAction(e -> onAddLight());
        Button removeLight = new Button("-");
        removeLight.setOnAction(e -> onRemoveLight());
        HBox lightBtns = new HBox(5, addLight, removeLight);
        lightsList.setPrefHeight(160);

        right.getChildren().addAll(
                title,
                resetBtn, rotateBtn, scaleBtn, translateBtn, colorBtn,
                new Separator(Orientation.HORIZONTAL),
                showMeshCheckBox, showTextureCheckBox, showIlluminationCheckBox, addTextureBtn,
                new Separator(Orientation.HORIZONTAL),
                lightsLabel, lightBtns, lightsList
        );
        return right;
    }

    private Node createStatusBar() {
        return null;
    }


    private void setupListeners() {

    }

    private void setupAnimationTimer() {

    }

    private void addCameraToList(String x, String y, String z) {
        float fx = Float.parseFloat(x);
        float fy = Float.parseFloat(y);
        float fz = Float.parseFloat(z);
    }

    private void onUseCamera() {

    }

    private void onLoadModel() {

    }

    private void onSaveModel() {

    }

    private void onRemoveModel() {

    }

    private void onAddCamera() {

    }

    private void onRemoveCamera() {
        
    }

    private void onResetModel() {

    }

    private void onRotate() {

    }

    private void onScale() {

    }

    private void onTranslate() {

    }

    private void onSelectColor() {

    }

    private void onShowMeshChanged() {

    }

    private void onShowTextureChanged() {

    }

    private void onShowIlluminationChanged() {

    }

    private void onAddTexture() {

    }

    private void onAddLight() {

    }

    private void onRemoveLight() {

    }

    private void showInfoDialog(String s) {

    }
}