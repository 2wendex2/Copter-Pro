import java.io.DataOutputStream;
import java.io.IOException;

public class Swall extends RectLeaf {
	protected byte flags;
	protected int sprite;
	
	public Swall(int x, int y, int w, int h, byte flags, int sprite) {
		super(x, y, w, h);
		this.flags = flags;
		this.sprite = sprite;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		super.generate(data);
		data.writeByte(flags);
		data.writeInt(sprite);
	}
}