import java.io.DataOutputStream;
import java.io.IOException;

/*
	Один из четырёх классов объектов
	Имеет в себе 4 инта: x, y, w, h
	Затем байт из флагов
	Соответственно: не убивать игрока, убивать врага, умирать от пули, умирать от бомбы
	Затем инт спрайт
*/

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