import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class read {
	
	public static HashMap<Integer,Integer> maps = new HashMap<Integer,Integer>();
	public static void main(String args[]){
		File file = new File("/home/charles/Desktop/jar/dauth.jar");
		File data = new File("/home/charles/Desktop/jar/data.h");
		File fin = new File("/home/charles/Desktop/jar/dauth2.jar");
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fosd = new FileOutputStream(data);
			//48-57, 65-90,97-122
			int k = 0;
			while((k=fis.read())>=0){
				Offset off = new Offset();
				int c = rank(k,off);
				if(maps.get(c)==null){
					maps.put(c, off.offset);
				}
				
				fosd.write(c);
				int res = k+off.offset;
				if(!(res>=48&&res<=57)&&!(res>=65&&res<=90)&&!(res>=97&&res<=122)){
					System.out.println("find a match "+c+":"+res +"/"+k);
				}
				fosd.write(res);
			}
			fis.close();
			fosd.close();
			
			//revert
			fis = new FileInputStream(data);
			fosd = new FileOutputStream(fin);
			
			int flag = 1;//index
			int op = 122;
			while((k=fis.read())>=0){
				if(flag ==1){
					op = k;
					flag = 0;
					continue;
				}else{
					flag = 1;
					int off = maps.get(op);
					fosd.write(k-off);
				}
			}
			fis.close();
			fosd.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("file len is "+file.length()+"/"+data.length());
	}
	
	private static int rank(int k,Offset off){
		int a =65;
		int dtype = 121;
		int remedy = 0;
		if(k>=128){
			k=k-128;
			a+=32;
			remedy = -128;
			dtype = 122;
		}
		//48-57, 65-90,97-122
		if(k>=0&&k<=25){
			off.offset = 97+remedy;
			return a;
		}else if(k>25&&k<48){
			off.offset = 71+remedy;
			return a+1;
		}else if(k>=48&&k<=57){
			off.offset = remedy;
			return dtype;
		}else if(k>57&&k<65){
			off.offset = 39+remedy;
			return a+2;
		}else if(k>=65&&k<=90){
			off.offset = remedy;
			return dtype;
		}else if(k>90&&k<97){
			off.offset = 6+remedy;
			return a+3;
		}else if(k>=97&&k<=122){
			off.offset = remedy;
			return dtype;
		}else if(k>122&&k<=127){
			off.offset = -5+remedy;
			return a+4;
		}
		off.offset = remedy;
		return dtype;
			
			
	}
	
	private static class Offset{
		int offset = 0;
	}

}
