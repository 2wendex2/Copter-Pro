import java.io.DataOutputStream;
import java.io.IOException;

class RectLeaf implements LevelGenerator {
	protected int x, y, w, h;
	
	public RectLeaf(int xA, int yA, int wA, int hA) {
		x = xA;
		y = yA;
		w = wA;
		h = hA;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		data.writeInt(x);
		data.writeInt(y);
		data.writeInt(w);
		data.writeInt(h);
	}
}