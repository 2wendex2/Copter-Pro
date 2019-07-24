import java.io.DataOutputStream;
import java.io.IOException;

interface LevelGenerator {
	void generate(DataOutputStream data) throws IOException;
}