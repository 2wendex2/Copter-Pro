import java.io.DataOutputStream;
import java.io.IOException;

/*
	Один из четырёх основных классов
	На самом деле определяется подид
	Простейшие содержат id, x и y
	Такой же, как Enemy, разделены для простоты
*/

public class Item implements LevelGenerator {
	protected int id;
	protected int x, y;
	
	public Item(int id, int x, int y) {
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