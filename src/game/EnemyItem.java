package game;

public interface EnemyItem extends Item, Updatable {
    void collisionEnemy(Enemy enemy);
}
