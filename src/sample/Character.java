package sample;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

// Pane - панель персонажа
public class Character extends Pane {
    Image image = new Image(getClass().getResourceAsStream("1.png"));
    ImageView imageView = new ImageView(image);

    public static final int PLAYER_SIZE = 32;
    private int count = 3;
    private int columns = 3;
    private int offSetY = 64;
    private int offSetX = 0;
    private int width = 32;
    private int height = 32;

    public SpriteAnimation animation;

    // точка, будет использоваться для прыжков
    // в каждый момент времени мы двигаем персонажа на chVelocity
    // если 0, то не двигаем
    public Point2D chVelocity = new Point2D(0, 0);
    private boolean canJump = true;

    public Character () {
        // видимая область картинки
        imageView.setViewport(new Rectangle2D(offSetX, offSetY, width, height));
        // создаем анимацию
        animation = new SpriteAnimation(this.imageView,
                Duration.millis(300), count, columns,
                offSetX, offSetY, width, height);

        // добавляем нашу картинку на панель персонажа
        getChildren().addAll(this.imageView);
    }

    public void moveX (int value) {
        boolean right = value > 0;
        // проходимся по всем значениям перемещения вправо
        for (int i = 0; i < Math.abs(value); i++) {
            // проходимся по всем нашим платформам
            for (Node platform : Main.platforms) {
                // если границы персонажа пересекают границу платформы
                if (this.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    // и если движемся вправо
                    if (right) {
                        // если персонаж соприкасается с блоком
                        if (this.getTranslateX() + PLAYER_SIZE == platform.getTranslateX()) {
                            // то останавливаем перса за 1 пиксель до платформы
                            this.setTranslateX(this.getTranslateX() - 1);
                            return;
                        }
                    } else {
                        if (this.getTranslateX() == platform.getTranslateX() + Block.BLOCK_SIZE) {
                            this.setTranslateX(this.getTranslateX() + 1);
                            return;
                        }
                    }
                }
            }

            // двигаем нашего персонажа
            this.setTranslateX(this.getTranslateX() + (right ? 1 : -1));
        }
    }

    public void moveY (int value) {
        boolean down = value > 0;
        for(int i = 0; i < Math.abs(value); i++){
            for(Block platform : Main.platforms){
                if(getBoundsInParent().intersects(platform.getBoundsInParent())){
                    if(down){
                        if(this.getTranslateY()+ PLAYER_SIZE == platform.getTranslateY()){
                            this.setTranslateY(this.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    } else{
                        if(this.getTranslateY() == platform.getTranslateY()+ Block.BLOCK_SIZE){
                            this.setTranslateY(this.getTranslateY() + 1);
                            chVelocity = new Point2D(0,10);
                            return;
                        }
                    }
                }
            }
        this.setTranslateY(this.getTranslateY() + (down ? 1 : -1));
        // если персонаж упал
        if (this.getTranslateY() > 645){
            // перемещаем персонажа в начало уровня
            this.setTranslateX(20);
            this.setTranslateY(400);
            // перемещаем камеру на начало уровня
            Main.gameRoot.setLayoutX(0);
        }
        }
    }


    //
    public void jump () {
        if (canJump) {
            chVelocity = chVelocity.add(0, -30);
            canJump = false;
        }
    }
}
