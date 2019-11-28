import java.io.DataOutputStream;
import java.io.IOException;

/*
	Главный класс реализация: RectLeaf
	Он принимает 4 инта x, y, w, h и generate записывает в файл
	Слишком очевидно
	Нужен для наследования
	Да и напрямую использовать никто не мешает
	Оч классный класс
*/

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