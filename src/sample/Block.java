package sample;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Block extends Pane {
    public static final int BLOCK_SIZE = 45;


    Image blockImg = new Image(getClass().getResourceAsStream("items.png"));
    ImageView block;

    public enum BlockType {
        PLATFORM, BRICK, BONUS, PIPE_TOP, PIPE_BOT, INVISIBLE, STONE
    }

    public Block (BlockType blockType, int x, int y) {
        block = new ImageView(blockImg);
        block.setFitHeight(BLOCK_SIZE);
        block.setFitWidth(BLOCK_SIZE);
        this.setTranslateX(x);
        this.setTranslateY(y);

        // в зависимости от типа платформы устанавливаем картинки
        switch (blockType) {
            case PLATFORM:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                break;
            case BRICK:
                block.setViewport(new Rectangle2D(16, 0, 16, 16));
                break;
            case BONUS:
                block.setViewport(new Rectangle2D(384, 0, 16, 16));
                break;
            case PIPE_TOP:
                block.setViewport(new Rectangle2D(0, 128, 32, 16));
                block.setFitWidth(BLOCK_SIZE * 2);
                break;
            case PIPE_BOT:
                block.setViewport(new Rectangle2D(0, 145, 32, 14));
                block.setFitWidth(BLOCK_SIZE * 2);
                break;
            case INVISIBLE:
                block.setViewport(new Rectangle2D(0, 0, 16, 16));
                block.setOpacity(0);
                break;
            case STONE:
                block.setViewport(new Rectangle2D(0, 16, 16, 16));
                break;
        }

        // добавляем наш блок на панель блока
        this.getChildren().add(block);
        // добавляем блок в коллекцию платформ
        Main.platforms.add(this);
        // отображаем наш блок в гейм рут
        Main.gameRoot.getChildren().add(this);
    }
}
