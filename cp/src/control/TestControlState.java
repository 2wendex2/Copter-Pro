package control;

public class TestControlState implements ControlState {
    private Graphics graphics;

    TestControlState(Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void draw() {
        graphics.fill(0.f, 1.f, 0.f, 1.f);
    }

    @Override
    public void keyboardKeyPress(int key) {

    }

    @Override
    public void keyboardKeyRelease(int key) {

    }


    @Override
    public void update() {

    }
}
