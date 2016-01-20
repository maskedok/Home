import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Master
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<Integer> test = new ArrayList<Integer>();
		
		for (int i = 0; i < 100; i++) {
			test.add(i);
		}
		
		for (int i = 0; i <100; i++) {
			System.out.println(test.remove(0));
		}
		
		System.out.println(test.size());
	}

}
