package sample;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;


public class Main extends Application {

    // создаем пункты главного меню
    MenuItem newGame = new MenuItem("Новая игра");
    MenuItem setting = new MenuItem("Настройки");
    MenuItem exit = new MenuItem("Выход");
    // добавляем в главное меню
    SubMenu main = new SubMenu(newGame, setting, exit);

    // пункты настроек
    MenuItem sound = new MenuItem("Sound");
    MenuItem video = new MenuItem("Video");
    MenuItem account = new MenuItem("Account setting");
    MenuItem toMain = new MenuItem("Main");
    SubMenu settings = new SubMenu(sound, video, account, toMain);

    // пункты меню
    MenuBox menuBox = new MenuBox(main);

    public static ArrayList<Block> platforms = new ArrayList<>();
    private HashMap<KeyCode, Boolean> keys = new HashMap<>();

    Image bg = new Image(getClass().getResourceAsStream("background.png"));

    public static Pane gameRoot = new Pane();
    public static Pane appRoot = new Pane();

    public Character player;
    // индекс уровня в массиве уровней
    public int levelNumber = 0;
    private int levelWidth;

    // метод будет выполнятся при запуске
    public void initContent () {
        ImageView bgView = new ImageView(bg);
        bgView.setFitWidth(212 * Block.BLOCK_SIZE);
        bgView.setFitHeight(14 * Block.BLOCK_SIZE);

        levelWidth = Level.levels[levelNumber] [0].length() * Block.BLOCK_SIZE;
        // добавляем наши блоки в зависимости от цифр в левел
        for (int i = 0; i < Level.levels[levelNumber].length; i++) {
            // записываем строку в лайн
            String line = Level.levels[levelNumber][i];
            // проходимся по строке
            for (int j = 0; j < line.length(); j++) {
                // получаем каждый символ строки и сравниваем
                // на каждый символ свой блок картинки
                switch (line.charAt(j)){
                    case '0':
                        break;
                    case '1':
                        Block platform = new Block(Block.BlockType.PLATFORM, j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;
                    case '2':
                        Block brick = new Block(Block.BlockType.BRICK,j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;
                    case '3':
                        Block stone = new Block(Block.BlockType.STONE,j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;
                    case '4':
                        Block PipeTopBlock = new Block(Block.BlockType.PIPE_TOP, j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;
                    case '5':
                        Block PipeBottomBlock = new Block(Block.BlockType.PIPE_BOT, j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;
                    case '*':
                        Block InvisibleBlock = new Block(Block.BlockType.INVISIBLE, j * Block.BLOCK_SIZE, i * Block.BLOCK_SIZE);
                        break;

                }
            }
        }

        player = new Character();
        player.setTranslateX(20);
        player.setTranslateY(400);

        // для сдвига фона относительно персонажа
        player.translateXProperty().addListener(((observableValue, number, t1) -> {
            int offset = t1.intValue();
            if (offset > 450 && offset < levelWidth - 450) {
                gameRoot.setLayoutX(-(offset - 450));
                bgView.setLayoutX(-(offset - 450));
            }
        }));

        // добавляем игрока на панель
        gameRoot.getChildren().add(player);
        appRoot.getChildren().addAll(bgView, gameRoot, menuBox);
    }


    // обработка нажатий
    private void update(){
        if(isPressed(KeyCode.UP) && player.getTranslateY() >= 5){
            player.jump();
        }
        if(isPressed(KeyCode.LEFT) && player.getTranslateX() >= 5){
            player.setScaleX(-1);
            player.animation.play();
            player.moveX(-3);
        }
        if(isPressed(KeyCode.RIGHT) && player.getTranslateX() + 32 <= levelWidth - 5){
            player.setScaleX(1);
            player.animation.play();
            player.moveX(3);
        }
        if(player.chVelocity.getY() < 10){
            player.chVelocity = player.chVelocity.add(0,1);
        }
        player.moveY((int)player.chVelocity.getY());
    }

    private boolean isPressed(KeyCode key){
        return keys.getOrDefault(key,false);
    }

    @Override
    public void start(Stage stage){
        initContent();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };
        animationTimer.start();


        // обработка нажатий на пункты меню
        setting.setOnMouseClicked(e -> {
            menuBox.setSubMenu(settings);
        });
        toMain.setOnMouseClicked(e -> {
            menuBox.setSubMenu(main);
        });
        // выход из игры
        exit.setOnMouseClicked(e -> {
            System.exit(0);
        });


        Scene scene= new Scene(appRoot, 900, 600);
        scene.setOnKeyPressed( e -> {
            keys.put(e.getCode(), true);
        });

        FadeTransition ft = new FadeTransition(Duration.seconds(1), menuBox);
        scene.setOnKeyReleased( e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                if (!menuBox.isVisible()) {
                    ft.setFromValue(0);
                    ft.setToValue(1);
                    ft.play();
                    menuBox.setVisible(true);
                } else {
                    ft.setFromValue(1);
                    ft.setToValue(0);
                    ft.setOnFinished(evt -> {
                        menuBox.setVisible(false);
                    });
                    ft.play();
                }
            } else {
                keys.put(e.getCode(), false);
                player.animation.stop();
            }
        });


        stage.setScene(scene);
        stage.setTitle("Slime - 1.0");
        stage.show();


    }

    // создаем меню
    // СтакПэйн - последний добавленный элемент отображается выше всего
    private static class MenuItem extends StackPane {
        // через конструктор создаем пункты меню
        public MenuItem (String name) {
            // фон пункта меню
            Rectangle bg = new Rectangle(200, 20, Color.WHITE);
            bg.setOpacity(0.5);


            // текст пунктов меню
            Text text = new Text(name);
            text.setFill(Color.BLACK);
            text.setFont(Font.font("Fantasy", FontWeight.BOLD, 14));

            this.setAlignment(Pos.CENTER);
            this.getChildren().addAll(bg, text); // т.к текст добавлен последним, он будет отображаться поверх бг

            // создаем анимацию смены цвета пунктов меню
            FillTransition ft = new FillTransition(Duration.seconds(0.5), bg);
            // когда наводим мышку
            this.setOnMouseEntered(e -> {
                ft.setFromValue(Color.LIGHTCYAN);
                ft.setToValue(Color.DARKGREEN);
                ft.setCycleCount(Animation.INDEFINITE); // анимация будет происходить бесконечно
                ft.setAutoReverse(true); // будет автоматически возвращаться к начальному значениюб
                ft.play();
            });

            // когда убираем мышку с пункта меню
            this.setOnMouseExited(e -> {
                ft.stop();
                bg.setFill(Color.WHITE);
            });



        }
    }

    // класс для отображения меню
    // содержит сабменюшки
    private static class MenuBox extends Pane {
        static SubMenu subMenu;
        public MenuBox(SubMenu subMenu) {
            MenuBox.subMenu = subMenu;

            // делаем наше меню невидимым при запуске
            this.setVisible(false);

            Image image = new Image(getClass().getResourceAsStream("Suu.png"));
            ImageView suuBg = new ImageView(image);
            suuBg.setFitHeight(600);
            suuBg.setFitWidth(900);
            suuBg.setOpacity(0.7);
            this.getChildren().addAll(suuBg, subMenu);
        }

        // для переключения между менюшками
        public void setSubMenu (SubMenu subMenu) {

            // удаляем предыдущее меню
            this.getChildren().remove(MenuBox.subMenu);
            // заменяем старую менюшку на новую (передается в параметре)
            MenuBox.subMenu = subMenu;
            // и отображаем её
            this.getChildren().add(MenuBox.subMenu);
        }
    }

    // класс для хранения наших пунктов меню
    private static class SubMenu extends VBox {
        public SubMenu (Main.MenuItem...items) { // храним сколько влезет итемов
            this.setSpacing(15); // отступы между пунктаим
            this.setTranslateX(100); // устанавливаем позицию нашего меню
            this.setTranslateY(50);

            // цикле добавляем все пункты в меню в наш бокс
            for (Main.MenuItem item : items) {
                this.getChildren().add(item);
            }
        }
    }




    public static void main(String[] args) {
        launch(args);
    }
}
