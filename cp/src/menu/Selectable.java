package menu;

import control.Drawable;

public abstract class Selectable implements Drawable {
    private Selectable left, right, up, down;

    public abstract void onPress();

    public Selectable getLeft() {
        return left;
    }

    public Selectable getRight() {
        return right;
    }

    public Selectable getUp() {
        return up;
    }

    public Selectable getDown() {
        return down;
    }

    public void setMarkUp(Selectable left, Selectable right, Selectable up, Selectable down) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.down = down;
    }

    public abstract void drawSelected();
}