import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//���������� x
//���������� y
//������
//������
//�����
//����
//����
//���������� ������� ��������
//{����������� ������� ��������}


//����������� ������:
//������������� ������
//���������� ��������
//{����������� ��������}

class LevelTree extends RectLeaf {
	private long flags;
	private int bullet;
	private int bomb;
	private HashMap<Integer, ArrayList<LevelGenerator>> objects;
	
	public LevelTree(int xA, int yA, int wA, int hA, long flags, int bullet, int bomb) {
		super(xA, yA, wA, hA);
		objects = new HashMap<>();
		this.flags = flags;
		this.bullet = bullet;
		this.bomb = bomb;
	}
	
	public void add(int id, LevelGenerator obj) {
		if (!objects.containsKey(id))
			objects.put(id, new ArrayList<LevelGenerator>());
		objects.get(id).add(obj);
	}
	
	public void generate(DataOutputStream data) throws IOException {
		super.generate(data);
		data.writeLong(flags);
		data.writeInt(bullet);
		data.writeInt(bomb);
		
		Set<Map.Entry<Integer, ArrayList<LevelGenerator>>> set = objects.entrySet();
		data.writeInt(set.size());
		for (Map.Entry<Integer, ArrayList<LevelGenerator>> i : set)
		{
			data.writeInt(i.getKey());
			data.writeInt(i.getValue().size());
			for (LevelGenerator j : i.getValue())
				j.generate(data);
		}
	}
}