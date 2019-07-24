import java.io.DataOutputStream;
import java.io.IOException;

public class Enemy implements LevelGenerator {
	protected int id;
	protected int x, y;
	
	public Enemy(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		data.writeInt(id);
		data.writeInt(x);
		data.writeInt(y);
	}
}