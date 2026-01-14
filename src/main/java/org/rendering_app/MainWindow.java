package org.rendering_app;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.rendering_app.math.DepthBuffer;
import org.rendering_app.math.PixelBuffer;
import org.rendering_app.model.Model;
import org.rendering_app.obj_utils.OBJReader;
import org.rendering_app.obj_utils.OBJWriter;
import org.rendering_app.render.Camera;
import org.rendering_app.render.Light;
import org.rendering_app.render.Material;
import org.rendering_app.render.RenderEngine;
import org.rendering_app.render.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {

    private final ListView<String> modelsList = new ListView<>();
    private final ListView<String> camerasList = new ListView<>();
    private final ListView<String> lightsList = new ListView<>();
    private final java.util.LinkedHashSet<Integer> activeModels = new java.util.LinkedHashSet<>();
    private final CheckBox showMeshCheckBox = new CheckBox("Show Mesh");
    private final CheckBox showTextureCheckBox = new CheckBox("Show Texture");
    private final CheckBox showIlluminationCheckBox = new CheckBox("Show Illumination");
    private final Label statusBar = new Label("Ready");
    private final Label fpsLabel = new Label("FPS: 0");
    private Canvas renderCanvas;
    private final List<Model> models = new ArrayList<>();
    private final List<Model> originalModels = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();
    private final List<Camera> cameras = new ArrayList<>();
    private Camera camera;
    private DepthBuffer depthBuffer;
    private PixelBuffer pixelBuffer;
    private RenderEngine renderEngine;
    private int selectedModel = -1;
    private long lastFpsTimeNs = 0;
    private int frameCount = 0;
    private double lastX;
    private double lastY;

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
        Button deletePolysBtn = new Button("Delete polygons");
        deletePolysBtn.setOnAction(e -> onDeletePolygons());
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
                resetBtn, deletePolysBtn, rotateBtn, scaleBtn, translateBtn, colorBtn,
                new Separator(Orientation.HORIZONTAL),
                showMeshCheckBox, showTextureCheckBox, showIlluminationCheckBox, addTextureBtn,
                new Separator(Orientation.HORIZONTAL),
                lightsLabel, lightBtns, lightsList
        );
        return right;
    }

    private Pane createStatusBar() {
        HBox status = new HBox(10);
        status.setPadding(new Insets(5, 10, 5, 10));
        HBox.setHgrow(statusBar, Priority.ALWAYS);
        status.getChildren().addAll(statusBar, fpsLabel);
        return status;
    }

    private void setupListeners() {
        modelsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        modelsList.getSelectionModel().getSelectedIndices().addListener(
                (javafx.collections.ListChangeListener<? super Integer>) c -> onModelsSelectionChanged()
        );

        renderCanvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        renderCanvas.setOnMouseDragged(e -> {
            if (e.getButton() != MouseButton.PRIMARY && !e.isPrimaryButtonDown()) return;
            double dx = e.getX() - lastX;
            double dy = e.getY() - lastY;
            org.rendering_app.math.Vector3D cameraPos = camera.getPosition();
            float radius = cameraPos.length();
            float theta = (float) Math.atan2(cameraPos.getZ(), cameraPos.getX());
            float phi = (float) Math.acos(cameraPos.getY() / radius);

            theta -= dx * 0.01f;
            phi = (float) Math.max(0.1f, Math.min(Math.PI - 0.1f, phi - dy * 0.01f));

            float newX = radius * (float) (Math.sin(phi) * Math.cos(theta));
            float newY = radius * (float) (Math.cos(phi));
            float newZ = radius * (float) (Math.sin(phi) * Math.sin(theta));
            camera.setPosition(new org.rendering_app.math.Vector3D(newX, newY, newZ));
            camera.setTarget(new org.rendering_app.math.Vector3D(0, 0, 0));
            lastX = e.getX();
            lastY = e.getY();

            updateScene();
        });

        renderCanvas.addEventHandler(ScrollEvent.SCROLL, e -> {
            double wheel = e.getDeltaY() > 0 ? -1 : 1;
            onMouseWheelMoved((float) wheel);
        });
    }

    private void setupAnimationTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateFps(now);
                updateScene();
            }
        };
        timer.start();
    }

    private void updateFps(long now) {
        if (lastFpsTimeNs == 0) lastFpsTimeNs = now;
        frameCount++;
        if (now - lastFpsTimeNs >= 1_000_000_000L) {
            double fps = frameCount * 1_000_000_000.0 / (now - lastFpsTimeNs);
            fpsLabel.setText(String.format("FPS: %.1f", fps));
            frameCount = 0;
            lastFpsTimeNs = now;
        }
    }

    private void updateScene() {
        double width = renderCanvas.getWidth();
        double height = renderCanvas.getHeight();
        if (width <= 2 || height <= 2) return;

        int w = (int) width;
        int h = (int) height;

        if (models.isEmpty()) {
            clearCanvas();
            return;
        }

        if (depthBuffer == null || depthBuffer.getWidth() != w || depthBuffer.getHeight() != h) {
            depthBuffer = new DepthBuffer(w, h);
            pixelBuffer = new PixelBuffer();
        } else {
            depthBuffer.clear();
            pixelBuffer.clear();
        }

        camera.setAspectRatio((float) w / (float) h);

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        try {
            java.awt.Graphics2D g2 = img.createGraphics();
            g2.setColor(new java.awt.Color(45, 45, 45));
            g2.fillRect(0, 0, w, h);
            g2.dispose();

            for (int i = 0; i < models.size(); i++) {
                Model model = models.get(i);
                Material material = materials.get(i);
                if (model == null || material == null) continue;

                renderEngine = new RenderEngine(camera, model, w, h, depthBuffer, pixelBuffer, material);
                renderEngine.render();
            }

            pixelBuffer.forEach((p, color) -> {
                int x = (int) p.getX();
                int y = (int) p.getY();
                if (x >= 0 && x < w && y >= 0 && y < h) {
                    img.setRGB(x, y, color.getRGB());
                }
            });

            drawBufferedImageOnCanvas(img);
        } catch (Exception ex) {
            ex.printStackTrace();
            statusBar.setText("Render error: " + ex.getMessage());
        }
    }

    private void drawBufferedImageOnCanvas(BufferedImage img) {
        WritableImage fxImage = new WritableImage(img.getWidth(), img.getHeight());
        javafx.embed.swing.SwingFXUtils.toFXImage(img, fxImage);
        GraphicsContext gc = renderCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, renderCanvas.getWidth(), renderCanvas.getHeight());
        gc.drawImage(fxImage, 0, 0);
    }

    private void clearCanvas() {
        GraphicsContext gc = renderCanvas.getGraphicsContext2D();
        gc.setFill(Color.rgb(45, 45, 45));
        gc.fillRect(0, 0, renderCanvas.getWidth(), renderCanvas.getHeight());
    }

    private void onModelsSelectionChanged() {
        activeModels.clear();
        activeModels.addAll(modelsList.getSelectionModel().getSelectedIndices());

        if (activeModels.isEmpty()) {
            selectedModel = -1;
            lightsList.getItems().clear();
            updateScene();
            return;
        }

        selectedModel = activeModels.iterator().next();

        Material mat = materials.get(selectedModel);
        showMeshCheckBox.setSelected(mat.isShowMesh());
        showTextureCheckBox.setSelected(mat.isShowTexture());
        showIlluminationCheckBox.setSelected(mat.isShowIllumination());

        refreshLightsList();
        updateScene();
    }


    private void onMouseWheelMoved(float wheel) {
        org.rendering_app.math.Vector3D cameraPos = camera.getPosition();
        float zoomSpeed = 3.0f;
        float radius = cameraPos.length();
        radius += wheel * zoomSpeed;
        radius = Math.max(1.0f, radius);
        cameraPos = cameraPos.normalize().multiply(radius);
        camera.setPosition(cameraPos);
        updateScene();
    }

    private void onLoadModel() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Model File");
        File file = chooser.showOpenDialog(null);
        if (file == null) return;

        try {
            String content = java.nio.file.Files.readString(java.nio.file.Path.of(file.getAbsolutePath()));
            Model model = OBJReader.read(content);
            models.add(model);
            originalModels.add(new Model(model));
            materials.add(new Material(false, false, false));
            modelsList.getItems().add(file.getName());
            selectedModel = models.size() - 1;
            modelsList.getSelectionModel().select(selectedModel);
            refreshModelsListTitles();
            refreshLightsList();
            statusBar.setText("Model loaded: " + file.getName());
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Failed to load model: " + ex.getMessage());
        }
    }

    private void onSaveModel() {
        if (!hasActiveModels()) return;

        javafx.stage.DirectoryChooser chooser = new javafx.stage.DirectoryChooser();
        chooser.setTitle("Choose folder to save selected models");
        File dir = chooser.showDialog(null);
        if (dir == null) return;

        String prefix = showInputDialog("Prefix for filenames:", "export");
        if (prefix == null) return;

        prefix = prefix.trim();
        if (prefix.isEmpty()) {
            showErrorDialog("Prefix cannot be empty.");
            return;
        }

        prefix = prefix.replaceAll("[\\\\/:*?\"<>|]", "_");

        int saved = 0;

        try {
            for (int idx : activeModels) {
                String rawName = String.valueOf(modelsList.getItems().get(idx));

                String cleaned = rawName.replaceFirst("^Model\\s+\\d+\\s*:\\s*", "");

                String base = cleaned.replaceAll("(?i)\\.obj$", "").trim();
                base = base.replaceAll("[\\\\/:*?\"<>|]", "_");
                if (base.isEmpty()) base = "model_" + idx;

                String nameNoExt = prefix + "_" + base;
                File out = new File(dir, nameNoExt + ".obj");

                int n = 2;
                while (out.exists()) {
                    out = new File(dir, nameNoExt + "_" + n + ".obj");
                    n++;
                }

                OBJWriter.write(models.get(idx), out.getAbsolutePath());
                saved++;
            }

            statusBar.setText("Saved " + saved + " model(s) to: " + dir.getAbsolutePath());
        } catch (Exception ex) {
            showErrorDialog("Failed to save models: " + ex.getMessage());
        }
    }

    private void onResetModel() {
        if (!hasActiveModels()) return;

        for (int idx : activeModels) {
            models.set(idx, new Model(originalModels.get(idx)));
        }

        statusBar.setText("Model(s) reset to original.");
        updateScene();
    }

    private void onDeletePolygons() {
        if (!hasActiveModels()) return;

        String s = showInputDialog("Polygon index or range:", "0-10");
        if (s == null) return;

        int from;
        int to;

        try {
            int[] r = org.rendering_app.model.ModelPolygonRemover.parseRange(s);
            from = r[0];
            to = r[1];
            if (from < 0 || to < 0) {
                showErrorDialog("Polygon index must be >= 0.");
                return;
            }
        } catch (Exception ex) {
            showErrorDialog("Example: 5 or 5-12.");
            return;
        }

        int totalRemoved = 0;

        try {
            for (int idx : activeModels) {
                Model m = models.get(idx);

                int removed = org.rendering_app.model.ModelPolygonRemover
                        .deletePolygonsRange(m, from, to);

                totalRemoved += removed;
            }

            statusBar.setText("Removed polygons: " + totalRemoved);
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Failed to delete polygons: " + ex.getMessage());
        }
    }

    private void onRemoveModel() {
        int idx = modelsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;

        models.remove(idx);
        originalModels.remove(idx);
        materials.remove(idx);
        modelsList.getItems().remove(idx);

        selectedModel = models.isEmpty() ? -1 : Math.min(idx, models.size() - 1);
        if (selectedModel != -1) {
            modelsList.getSelectionModel().select(selectedModel);
        }
        refreshModelsListTitles();
        refreshLightsList();
        updateScene();
        onModelsSelectionChanged();
    }

    private void onModelSelected() {
        int idx = modelsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        selectedModel = idx;
        Material mat = materials.get(selectedModel);
        showMeshCheckBox.setSelected(mat.isShowMesh());
        showTextureCheckBox.setSelected(mat.isShowTexture());
        showIlluminationCheckBox.setSelected(mat.isShowIllumination());
        refreshLightsList();
        updateScene();
    }

    private void refreshModelsListTitles() {
        for (int i = 0; i < modelsList.getItems().size(); i++) {
            String raw = modelsList.getItems().get(i);
            String cleaned = raw.replaceFirst("^Model\\s+\\d+\\s*:\\s*", "");
            modelsList.getItems().set(i, "Model " + i + ": " + cleaned);
        }
    }

    private void onAddCamera() {
        String s = showInputDialog("Position x,y,z:", "0,0,100");
        if (s == null) return;
        String[] parts = s.split(",");
        if (parts.length != 3) {
            showErrorDialog("Use x,y,z");
            return;
        }
        addCameraToList(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }

    private void addCameraToList(String x, String y, String z) {
        float fx = Float.parseFloat(x);
        float fy = Float.parseFloat(y);
        float fz = Float.parseFloat(z);
        Camera cam = new Camera(
                new org.rendering_app.math.Vector3D(fx, fy, fz),
                new org.rendering_app.math.Vector3D(0, 0, 0),
                1.0f, 1.0f, 0.01f, 1000.0f
        );
        cameras.add(cam);
        camerasList.getItems().add(
                String.format("Camera %d (%.1f, %.1f, %.1f)", cameras.size() - 1, fx, fy, fz)
        );
    }

    private void onUseCamera() {
        int idx = camerasList.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= cameras.size()) return;
        this.camera = cameras.get(idx);
        statusBar.setText("Using camera: " + camerasList.getItems().get(idx));
        updateScene();
    }

    private void onRemoveCamera() {
        int idx = camerasList.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= cameras.size()) return;
        cameras.remove(idx);
        camerasList.getItems().remove(idx);
        for (int i = 0; i < camerasList.getItems().size(); i++) {
            String s = camerasList.getItems().get(i);
            s = s.replaceFirst("^Camera\\s+\\d+", "Camera " + i);
            camerasList.getItems().set(i, s);
        }
        if (!cameras.isEmpty()) {
            int newIdx = Math.min(idx, cameras.size() - 1);
            camerasList.getSelectionModel().select(newIdx);
            this.camera = cameras.get(newIdx);
        }
        updateScene();
    }

    private void onRotate() {
        if (!hasActiveModels()) return;

        String s = showInputDialog("Angles x,y,z (deg):", "0,0,0");
        if (s == null) return;

        String[] a = s.split(",");
        if (a.length != 3) {
            showErrorDialog("Use x,y,z");
            return;
        }

        try {
            float rx = Float.parseFloat(a[0].trim());
            float ry = Float.parseFloat(a[1].trim());
            float rz = Float.parseFloat(a[2].trim());

            for (int idx : activeModels) {
                org.rendering_app.math.GraphicConveyor.rotate(models.get(idx), rx, ry, rz);
            }

            statusBar.setText(String.format("Rotated %d model(s): %.1f, %.1f, %.1f",
                    activeModels.size(), rx, ry, rz));
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Invalid angles.");
        }
    }

    private void onScale() {
        if (!hasActiveModels()) return;

        String s = showInputDialog("Scale x,y,z:", "1,1,1");
        if (s == null) return;

        String[] a = s.split(",");
        if (a.length != 3) {
            showErrorDialog("Use x,y,z");
            return;
        }

        try {
            float sx = Float.parseFloat(a[0].trim());
            float sy = Float.parseFloat(a[1].trim());
            float sz = Float.parseFloat(a[2].trim());

            for (int idx : activeModels) {
                org.rendering_app.math.GraphicConveyor.scale(models.get(idx), sx, sy, sz);
            }

            statusBar.setText(String.format("Scaled %d model(s): %.2f, %.2f, %.2f",
                    activeModels.size(), sx, sy, sz));
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Invalid scale.");
        }
    }

    private void onTranslate() {
        if (!hasActiveModels()) return;

        String s = showInputDialog("Translation x,y,z:", "0,0,0");
        if (s == null) return;

        String[] a = s.split(",");
        if (a.length != 3) {
            showErrorDialog("Use x,y,z");
            return;
        }

        try {
            float tx = Float.parseFloat(a[0].trim());
            float ty = Float.parseFloat(a[1].trim());
            float tz = Float.parseFloat(a[2].trim());

            for (int idx : activeModels) {
                org.rendering_app.math.GraphicConveyor.translate(models.get(idx), tx, ty, tz);
            }

            statusBar.setText(String.format("Translated %d model(s): %.2f, %.2f, %.2f",
                    activeModels.size(), tx, ty, tz));
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Invalid translation.");
        }
    }

    private void onSelectColor() {
        if (selectedModel == -1) {
            showInfoDialog("Select a model first.");
            return;
        }
        Material mat = materials.get(selectedModel);
        java.awt.Color current = mat.getBaseColor();
        Color fxStart = Color.rgb(current.getRed(), current.getGreen(), current.getBlue());

        Color chosen = showColorDialog(fxStart);
        if (chosen == null) return;

        java.awt.Color awtColor = new java.awt.Color(
                (float) chosen.getRed(),
                (float) chosen.getGreen(),
                (float) chosen.getBlue()
        );
        mat.setBaseColor(awtColor);
        statusBar.setText("Color changed.");
        updateScene();
    }

    private void onShowMeshChanged() {
        if (!hasActiveModels()) return;
        boolean v = showMeshCheckBox.isSelected();
        for (int idx : activeModels) materials.get(idx).setShowMesh(v);
        updateScene();
    }

    private void onShowTextureChanged() {
        if (!hasActiveModels()) return;
        boolean v = showTextureCheckBox.isSelected();
        for (int idx : activeModels) materials.get(idx).setShowTexture(v);
        updateScene();
    }

    private void onShowIlluminationChanged() {
        if (!hasActiveModels()) return;
        boolean v = showIlluminationCheckBox.isSelected();
        for (int idx : activeModels) materials.get(idx).setShowIllumination(v);
        updateScene();
    }

    private void onAddTexture() {
        if (selectedModel == -1) {
            showInfoDialog("Select a model first.");
            return;
        }
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Texture File");
        File f = chooser.showOpenDialog(null);
        if (f == null) return;
        try {
            BufferedImage img = ImageIO.read(f);
            Texture tex = new Texture(img);
            Material mat = materials.get(selectedModel);
            mat.setTexture(tex);
            showTextureCheckBox.setSelected(true);
            statusBar.setText("Texture loaded: " + f.getName());
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Failed to load texture: " + ex.getMessage());
        }
    }

    private void onAddLight() {
        if (selectedModel == -1) {
            showInfoDialog("Select a model first.");
            return;
        }
        Color fxColor = showColorDialog(Color.WHITE);
        if (fxColor == null) return;

        String s = showInputDialog("Light position x,y,z:", "0,0,0");
        if (s == null) return;
        String[] parts = s.split(",");
        if (parts.length != 3) {
            showErrorDialog("Use x,y,z");
            return;
        }
        try {
            float x = Float.parseFloat(parts[0].trim());
            float y = Float.parseFloat(parts[1].trim());
            float z = Float.parseFloat(parts[2].trim());

            java.awt.Color awtColor = new java.awt.Color(
                    (float) fxColor.getRed(),
                    (float) fxColor.getGreen(),
                    (float) fxColor.getBlue()
            );

            Light light = new Light(awtColor, new org.rendering_app.math.Vector3D(x, y, z));
            materials.get(selectedModel).getLights().add(light);
            refreshLightsList();
            updateScene();
        } catch (Exception ex) {
            showErrorDialog("Invalid light position.");
        }
    }

    private void onRemoveLight() {
        if (selectedModel == -1) return;
        int idx = lightsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) return;
        List<Light> ls = materials.get(selectedModel).getLights();
        if (idx >= ls.size()) return;
        ls.remove(idx);
        refreshLightsList();
        updateScene();
    }

    private void refreshLightsList() {
        lightsList.getItems().clear();
        if (selectedModel == -1) return;
        List<Light> ls = materials.get(selectedModel).getLights();
        for (int i = 0; i < ls.size(); i++) {
            Light l = ls.get(i);
            org.rendering_app.math.Vector3D p = l.getPosition();
            lightsList.getItems().add(
                    String.format("Light %d (%.1f, %.1f, %.1f)", i, p.getX(), p.getY(), p.getZ())
            );
        }
    }

    private boolean hasActiveModels() {
        if (activeModels.isEmpty()) {
            showInfoDialog("Select at least one model first.");
            return false;
        }
        return true;
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText("Error");
        alert.showAndWait();
    }

    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setHeaderText("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String showInputDialog(String header, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setHeaderText(header);
        return dialog.showAndWait().orElse(null);
    }

    private Color showColorDialog(Color initial) {
        ColorPicker picker = new ColorPicker(initial);
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Choose color");
        dialog.getDialogPane().setContent(picker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK ? picker.getValue() : null);
        return dialog.showAndWait().orElse(null);
    }
}