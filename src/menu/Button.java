package menu;

import control.Sprite;

public abstract class Button extends Selectable {
    private Sprite sprite;
    private Sprite selectedSprite;
    private int x, y, selectedX, selectedY;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Button(Sprite sprite, Sprite selectedSprite, int x, int y, int selectedX, int selectedY) {
        this.sprite = sprite;
        this.selectedSprite = selectedSprite;
        this.x = x;
        this.y = y;
        this.selectedX = selectedX;
        this.selectedY = selectedY;
    }

    public void draw() {
        sprite.draw(x, y);
    }

    public void drawSelected() {
        selectedSprite.draw(selectedX, selectedY);
    }
}
