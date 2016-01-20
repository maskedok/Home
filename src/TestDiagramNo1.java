import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestDiagramNo1 {
	static String spfile_name_ = "F:/クラウド/Dropbox/デスクトップ/Common-Differential-amplifer49s4-db.sp";

//	メイン
	public static void main(String[] args) throws InterruptedException, IOException{
		System.out.println("Program Start");
		Pattern SPFILE_ELEMENT_PATTERN = Pattern.compile("(?:(^M\\w+) (\\w+) (\\w+) "
				+ "(\\w+) (\\w+) (cmos(?:p|n)))|(?:((^R|^C|)\\w+) (\\w+) (\\w+))",Pattern.CASE_INSENSITIVE);
		
		HashMap<String, String> np_channel = new HashMap<String, String>();
		
		//		4n=上端子　1+4n=素子　2+4n=下端子　3+4n=ゲート n=0~
		ArrayList<String> element_card_ = new ArrayList<String>();

		//		spファイルから抽出
		BufferedReader br = new BufferedReader(new FileReader(spfile_name_));
		String tmp_read;
		while((tmp_read = br.readLine())!=null){
			Matcher spfile_element_Matcher = SPFILE_ELEMENT_PATTERN.matcher(tmp_read);
			if (spfile_element_Matcher.find()) {
				if ("cmosn".equals(spfile_element_Matcher.group(6))) {
					element_card_.add(spfile_element_Matcher.group(2));
					element_card_.add(spfile_element_Matcher.group(1));
					element_card_.add(spfile_element_Matcher.group(4));
					element_card_.add(spfile_element_Matcher.group(3));
					np_channel.put(spfile_element_Matcher.group(1), "n");
				}else if ("cmosp".equals(spfile_element_Matcher.group(6))) {
					element_card_.add(spfile_element_Matcher.group(4));
					element_card_.add(spfile_element_Matcher.group(1));
					element_card_.add(spfile_element_Matcher.group(2));
					element_card_.add(spfile_element_Matcher.group(3));
					np_channel.put(spfile_element_Matcher.group(1), "p");
				}else if ("R".equals(spfile_element_Matcher.group(8))||
						  "r".equals(spfile_element_Matcher.group(8))||
						  "C".equals(spfile_element_Matcher.group(8))||
						  "c".equals(spfile_element_Matcher.group(8))) {
					element_card_.add(spfile_element_Matcher.group(9));
					element_card_.add(spfile_element_Matcher.group(7));
					element_card_.add(spfile_element_Matcher.group(10));
					element_card_.add("    ");
				}
			}
		}
		
		System.out.println("抽出に成功しました");
		
		
		
//		回路図作成
		String[][] diagram = new String[200][200];
		for (int i=0; i < 10; i++) {
			for (int j = 0; j <40; j++) {
				diagram[i][j] = "   ";
			}
		}
		int h=0;
		int w=3;
		String bottom_node = "vdd";
//		リストか０か判定
		while (element_card_.size()!=0) {
//			下端子に合う素子を探索（初期はvdd）
			for (int i = 0; i <= element_card_.size(); i=i+4) {
				if (i==element_card_.size()) {
					System.out.println("これ以上の探索は無用");
					h=h-2;
					bottom_node=diagram[h][w-3];
					System.out.println("端子をさらにひとつ戻す"+bottom_node);
//					if (bottom_node.equals("vdd")) {
//						w=w+4;
//						System.out.println("列変更");
//					}
					i=i-element_card_.size();
				}
				System.out.println("リストの中");
				for (int k = 0; k < element_card_.size(); k=k+4) {
					System.out.print(element_card_.get(k)+" ");
					System.out.print(element_card_.get(k+1)+" ");
					System.out.print(element_card_.get(k+2)+" ");
					System.out.print(element_card_.get(k+3));
					System.out.println();
				}
//				System.out.println("繰り返し変数i="+i);
				if (element_card_.get(i).equals(bottom_node)) {
					diagram[h][w] = element_card_.remove(i);
					System.out.println(h+" "+w+" "+diagram[h][w]);
					h++;
					diagram[h][w] = element_card_.remove(i);
					System.out.println(h+" "+w+" "+diagram[h][w]);
					bottom_node   = element_card_.remove(i);
					System.out.println("下"+bottom_node);
					diagram[h][w+1] = element_card_.remove(i);
					System.out.println(h+" "+(w+1)+" "+diagram[h][w+1]);
					h++;
					i=i-4;
					System.out.println();
				}
				if (bottom_node.equals("vss")) {
					System.out.println("vss端子に到達");
					h=h-2;
					bottom_node = diagram[h][w];
					w=w+3;
					System.out.println("端子をひとつ戻す"+bottom_node);
				}
			}
		}
		
		
		
//		リストの残りを出力
		for (int i = 0; i < element_card_.size(); i=i+4) {
			System.out.println(element_card_.get(i));
		}
		
	}
}
