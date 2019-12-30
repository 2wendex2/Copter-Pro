package game;

public class PlayerWrapWinner extends Player {
    public PlayerWrapWinner(int xA, int yA, int state, int bulletCount, int bombCount) {
        super(xA, yA, state, bulletCount, bombCount);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        if (outerRect(Level.getInstance().getW(), Level.getInstance().getH()))
            Game.getInstance().levelComplete();
    }
}
