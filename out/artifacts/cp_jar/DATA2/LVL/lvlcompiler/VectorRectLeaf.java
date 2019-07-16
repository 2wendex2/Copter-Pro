import java.io.DataOutputStream;
import java.io.IOException;

class VectorRectLeaf extends RectLeaf {
	protected int xVect;
	
	public VectorRectLeaf(int xA, int yA, int wA, int hA, int xVectA) {
		super(xA, yA, wA, hA);
		xVect = xVectA;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		super.generate(data);
		data.writeInt(xVect);
	}
}