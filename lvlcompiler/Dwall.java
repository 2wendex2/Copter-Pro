import java.io.DataOutputStream;
import java.io.IOException;

/*
	Один из четырёх классов объектов
	По сути частный случай Swall
	Но он настолько большой и страшный, что проще выделить в отдельный классов
	Начало повторяет Swall (поэтому он собсна его наследник)
	Имеет в себе 4 инта: x, y, w, h
	Затем байт из флагов
	Соответственно: не убивать игрока, убивать врага, умирать от пули, умирать от бомбы
	Затем инт спрайт
	
	Дальше начинается пиздец
	Байт dflags — ещё флаги, дабы не перекрывать те флаги
	Вертикаль не определена, горизонталь не определена соответственно (то есть идти в определённом направлении или нет)
	Два инта xr, yr: степень рандома, правые границы рандомности спауна этих стен
	xspeed, yspeed: соответственно, компоненты скорости
	
	На самом деле хуёво, надо бы переписать
	Создать какой нить подидентификатор, а то пездец же
	TODO: Обязательно поебусь на третьем эпизоде!
*/
	
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