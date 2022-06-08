import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Driver {
	public static void main(String[] args) throws IOException {
		FileWriter myWriter = new FileWriter("exp_log.txt");
		int numExp = 1;

		int correct = 0;
		int correctMode = 0;
		for(int i = 1; i <= numExp;i ++) {
			System.out.println("==============================================");
			Frame frame0 = new Frame(7, 2, 8);//Frame(int numTrials, int type, int center)
			System.out.println("ground truth:");
			printArr(frame0.groundTruth);
			System.out.println("pedicted :");
			printArr(frame0.predicted);
			int modeVotedResult = findCenter_mode(frame0.predicted);//
			//System.out.println("mode Voted Result: "+modeVotedResult);
			if (modeVotedResult ==0)
				correctMode ++;

		}
				//System.out.println(String.format("ARROW: offset: %d, thresh: %d,topK: %d correct: %d", offset, thresh, topK, correct));
				//myWriter.write(String.format("ARROW: offset: %d, thresh: %d,topK: %d, correct: %d \n", offset, thresh, topK, correct));
				
				//System.out.println(String.format("MODE: offset: %d, thresh: %d,topK: %d correct: %d", offset, thresh, topK, correctMode));
				//myWriter.write(String.format("MODE: offset: %d, thresh: %d,topK: %d, correct: %d \n", offset, thresh, topK, correctMode));
				//System.out.println("arrow: "+correct);
				//System.out.println("mode: "+correctMode);

		myWriter.close();
		
	}
	
	
	static void printArr(int[][] arr) {
		for(int i = 0; i < arr.length;i ++) {
			for(int j = 0; j < arr[i].length;j ++) {
				System.out.print(arr[i][j]+"	");
			}
			System.out.println();
		}
	}
	
	static void printArr(double[] arr) {
		for(int i = 0; i < arr.length;i ++) {
			System.out.print(arr[i] + "\t");
		}
		System.out.println();
	}
	
	static void printNum(int num) {
		for(int i = 1; i <= num;i ++) {
			System.out.print(i + "\t");
		}
		System.out.println();
	}
	
	static void printNum0(int num) {
		for(int i = 0; i <= num;i ++) {
			System.out.print(i + "\t");
		}
		System.out.println();
	}
	
	static void printArr(int[] arr) {
		for(int i = 0; i < arr.length;i ++) {
			System.out.print(arr[i] + "\t");
		}
		System.out.println();
	}
	

	static int findMax(int[][] arr) {
		int max = 0;
		for(int i = 0; i < arr.length;i ++) {
			for(int j = 0; j < arr[i].length;j ++) {
				if(max<arr[i][j]) {
					max = arr[i][j];
				}
			}
		}
		return max;
	}
	
	static int findMin(int[][] arr) {
		int min = 1000;
		for(int i = 0; i < arr.length;i ++) {
			for(int j = 0; j < arr[i].length;j ++) {
				if(min>arr[i][j]) {
					min = arr[i][j];
				}
			}
		}
		return min;
	}
	
	
	
	static int findCenter_mode(int[][] arr) {
		int[] flattened = new int[16];
		int[] modeVals = new int[16];
		int[] modeCnt = new int[16];
		for(int i = 0; i < 4;i ++) {
			for(int j = 0; j < 4;j ++) {
				flattened[i*4+j] = arr[i][j];
			}
		}
		for(int i = 0; i < 16;i ++) {
			int[] ans = getMode(assumeCenter(flattened, i));//ans[0] = modeVal; ans[1] = modeCount
			modeVals[i] = ans[0];
			modeCnt[i] = ans[1];
		}
		printNum0(15);
		System.out.println("modeVals");
		printArr(modeVals);
		System.out.println("modeCnt");
		printArr(modeCnt);
		
		//find max
		int maxVal = modeCnt[0];
		int maxIdx = 0;
		for(int i = 1; i < 16;i ++) {
			if(modeCnt[i] > maxVal) {
				maxVal = modeCnt[i];
				maxIdx = i;
			}
		}
		return maxIdx;
	}
	
	static int[] getMode(int[] arr) {
		int[] occ = new int[arr.length];
		for(int target = 0 ;target < arr.length;target ++) {
			for(int j = 0 ; j < arr.length;j ++) {
				if(arr[target]==arr[j]) {
					occ[target] ++;
				}
			}
		}
		
		//find max
		int maxIdx = 0;
		int maxVal = occ[0];
		for(int i = 1; i < occ.length;i ++) {
			if(occ[i] > maxVal) {
				maxIdx = i;
				maxVal = occ[i];
			}
		}
		int[] ans = new int[2];//ans[0] = modeVal; ans[1] = modeCount
		ans[0] = arr[maxIdx]; 
		ans[1] = occ[maxIdx];
		return ans;
	}
	
	static int[] assumeCenter(int[] arr, int center) {
		int[] adjusted = new int[arr.length];
		if(center==0) {
			adjusted[0] = arr[0];
			adjusted[1] = arr[1]-5;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-5;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-15;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-15;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-15;
			adjusted[14] = arr[14]-15;
			adjusted[15] = arr[15]-15;
		}
		if(center==1) {
			adjusted[0] = arr[0]-5;
			adjusted[1] = arr[1];
			adjusted[2] = arr[2]-5;
			adjusted[3] = arr[3]-10;
			adjusted[4] = arr[4]-5;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-15;
			adjusted[14] = arr[14]-15;
			adjusted[15] = arr[15]-15;
		}
		if(center==2) {
			adjusted[0] = arr[0]-10;
			adjusted[1] = arr[1]-5;
			adjusted[2] = arr[2];
			adjusted[3] = arr[3]-5;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-5;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-15;
			adjusted[14] = arr[14]-15;
			adjusted[15] = arr[15]-15;
		}
		if(center==3) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-5;
			adjusted[3] = arr[3];
			adjusted[4] = arr[4]-15;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-5;
			adjusted[8] = arr[8]-15;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-15;
			adjusted[14] = arr[14]-15;
			adjusted[15] = arr[15]-15;
		}
		if(center==4) {
			adjusted[0] = arr[0]-5;
			adjusted[1] = arr[1]-5;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4];
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-15;
			adjusted[8] = arr[8]-5;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-15;
			adjusted[12] = arr[12]-10;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-15;
		}
		if(center==5) {
			adjusted[0] = arr[0]-5;
			adjusted[1] = arr[1]-5;
			adjusted[2] = arr[2]-5;
			adjusted[3] = arr[3]-10;
			adjusted[4] = arr[4]-5;
			adjusted[5] = arr[5];
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-5;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-10;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-10;
		}
		if(center==6) {
			adjusted[0] = arr[0]-10;
			adjusted[1] = arr[1]-5;
			adjusted[2] = arr[2]-5;
			adjusted[3] = arr[3]-5;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6];
			adjusted[7] = arr[7]-5;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-5;
			adjusted[12] = arr[12]-10;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-10;
		}
		if(center==7) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-5;
			adjusted[3] = arr[3]-5;
			adjusted[4] = arr[4]-15;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7];
			adjusted[8] = arr[8]-15;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-5;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-10;
		}
		if(center==8) {
			adjusted[0] = arr[0]-10;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-5;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-15;
			adjusted[8] = arr[8];
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-15;
			adjusted[12] = arr[12]-5;
			adjusted[13] = arr[13]-5;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-15;
		}
		if(center==9) {
			adjusted[0] = arr[0]-10;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-10;
			adjusted[4] = arr[4]-5;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-5;
			adjusted[9] = arr[9];
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-5;
			adjusted[13] = arr[13]-5;
			adjusted[14] = arr[14]-5;
			adjusted[15] = arr[15]-10;
		}
		if(center==10) {
			adjusted[0] = arr[0]-10;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-10;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-5;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-5;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10];
			adjusted[11] = arr[11]-5;
			adjusted[12] = arr[12]-10;
			adjusted[13] = arr[13]-5;
			adjusted[14] = arr[14]-5;
			adjusted[15] = arr[15]-5;
		}
		if(center==11) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-10;
			adjusted[2] = arr[2]-10;
			adjusted[3] = arr[3]-10;
			adjusted[4] = arr[4]-15;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-5;
			adjusted[7] = arr[7]-5;
			adjusted[8] = arr[8]-15;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11];
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-5;
			adjusted[15] = arr[15]-5;
		}
		if(center==12) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-15;
			adjusted[2] = arr[2]-15;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-15;
			adjusted[8] = arr[8]-5;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-10;
			adjusted[11] = arr[11]-15;
			adjusted[12] = arr[12];
			adjusted[13] = arr[13]-5;
			adjusted[14] = arr[14]-10;
			adjusted[15] = arr[15]-15;
		}
		if(center==13) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-15;
			adjusted[2] = arr[2]-15;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-5;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-10;
			adjusted[12] = arr[12]-5;
			adjusted[13] = arr[13];
			adjusted[14] = arr[14]-5;
			adjusted[15] = arr[15]-10;
		}
		if(center==14) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-15;
			adjusted[2] = arr[2]-15;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-10;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-10;
			adjusted[9] = arr[9]-5;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-5;
			adjusted[12] = arr[12]-10;
			adjusted[13] = arr[13]-5;
			adjusted[14] = arr[14];
			adjusted[15] = arr[15]-5;
		}
		if(center==15) {
			adjusted[0] = arr[0]-15;
			adjusted[1] = arr[1]-15;
			adjusted[2] = arr[2]-15;
			adjusted[3] = arr[3]-15;
			adjusted[4] = arr[4]-15;
			adjusted[5] = arr[5]-10;
			adjusted[6] = arr[6]-10;
			adjusted[7] = arr[7]-10;
			adjusted[8] = arr[8]-15;
			adjusted[9] = arr[9]-10;
			adjusted[10] = arr[10]-5;
			adjusted[11] = arr[11]-5;
			adjusted[12] = arr[12]-15;
			adjusted[13] = arr[13]-10;
			adjusted[14] = arr[14]-5;
			adjusted[15] = arr[15];
		}
		return adjusted;
	}
}
