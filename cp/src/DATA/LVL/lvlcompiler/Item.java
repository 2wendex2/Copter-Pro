import java.io.DataOutputStream;
import java.io.IOException;

public class Item extends RectLeaf {
	protected int id;
	protected int sprite;
	
	public Item(int x, int y, int w, int h, int id, int sprite) {
		super(x, y, w, h);
		this.id = id;
		this.sprite = sprite;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		super.generate(data);
		data.writeInt(id);
		data.writeInt(sprite);
	}
}