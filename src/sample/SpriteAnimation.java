package sample;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class SpriteAnimation  extends Transition{
    private final ImageView characterImg;
    private final int count;
    private final int columns;
    private int offsetX;
    private int offsetY;
    private final int width;
    private final int height;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count, int columns,
            int offsetX, int offsetY,
            int width, int height
    ){
        this.characterImg = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.setCycleDuration(duration);
        this.setCycleCount(Animation.INDEFINITE); // бесконечная анимация
        this.setInterpolator(Interpolator.LINEAR); // анимация без эффектов (замедление и т.д)
        // устанавливаем наш квадрат со спрайтами
        this.characterImg.setViewport(new Rectangle2D(offsetX, offsetY, width, height));

    }
    public void setOffsetX (int x) { this.offsetX = x; }
    public void setOffsetY (int y){
        this.offsetY = y;
    }

    // метод определяет поведение анимации
    @Override
    protected void interpolate(double k) {
        final int index = Math.min((int)Math.floor(count * k), count - 1);
        final int x = (index % columns) * width + offsetX;
        final int y = (index / columns) * height + offsetY;
        // создаем прямоугольник с координатами верхнего
        // левого угла х, у
        // будет отображаться картинка по этим координатам
        characterImg.setViewport(new Rectangle2D(x, y, width, height));
    }
}