package game;

public interface PlayerItem extends Item {
    void collisionPlayer(Player player);
    boolean deathFromPlayer();
}