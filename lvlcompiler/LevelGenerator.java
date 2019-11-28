import java.io.DataOutputStream;
import java.io.IOException;

/*
	Интерфейс для генерации уровней
	Имеет метод generate, который просто записывает данный объект в файл
	
	Вообще суть всех уровней:
	Предустановленные объекты: те, которые находятся в файле уровня
	Непредустановленные: создаваемые во время процесса игры
	Как можно догадаться, генератор уровней не контролирует непредустановленные
	
	Все пред объекты делятся на классы: стена, движущаяся стена, враг, итем
	В начале находится количество классов (int)
	Потом идут описания
	Описание начинается с идентефикатора класса (int), соотвественно: 0 стена, 1 движ стена, 2 враг, 3 итем
	Далее их описание
	Описание каждого из классов здесь описывать не буду
	Если хотите, зырьте: Swall.java, Dwall.java, Enemy.java, Item.java
	Каждый из них записывает один объект, а не весь класс целиком, это так, к слову
	
	Главный класс реализация: RectLeaf
	Он принимает 4 инта x, y, w, h и generate записывает в файл
*/

interface LevelGenerator {
	void generate(DataOutputStream data) throws IOException;
}