package ynu.util;

public class BeanUtils {
	
	public static int[] toIntArr(String str) {
		
		char arr[]=str.toCharArray();
		int auth[]=new int[5];
		for (int i = 0; i<5; i++) {
			if(arr[i]=='1'){
				auth[i]=1;
			}else {
				auth[i]=0;
			}
		}
		return auth;
	}

}
