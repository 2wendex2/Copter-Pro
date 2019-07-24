import java.io.DataOutputStream;
import java.io.IOException;

public class Dwall extends Swall {
	protected byte dflags;
	protected int xr, yr;
	protected int xspeed, yspeed;
	
	public Dwall(int x, int y, int w, int h, byte flags, int sprite, byte dflags, int xr, int yr, int xspeed, int yspeed) {
		super(x, y, w, h, flags, sprite);
		this.dflags = dflags;
		this.xr = xr;
		this.yr = yr;
		this.xspeed = xspeed;
		this.yspeed = yspeed;
	}
	
	public void generate(DataOutputStream data) throws IOException {
		super.generate(data);
		data.writeByte(dflags);
		data.writeInt(xr);
		data.writeInt(yr);
		data.writeInt(xspeed);
		data.writeInt(yspeed);
	}
}