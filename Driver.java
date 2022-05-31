import java.util.Random;

public class Driver {
	public static void main(String[] args) {
		/*
		System.out.println("type 0");
		int[][] arr = genGroundTruth(0);	
		printArr(arr);
		System.out.println();
		
		System.out.println("type 1");
		arr = genGroundTruth(1);	
		printArr(arr);
		System.out.println();
		
		System.out.println("type 2");
		arr = genGroundTruth(2);	
		printArr(arr);
		System.out.println();
		
		System.out.println("type 3");
		arr = genGroundTruth(3);	
		printArr(arr);
		System.out.println();
		
		System.out.print("distance2Prob(3, 2)");
		double[] prob = distance2Prob(3, 2);// distance, type
		printArr(prob);
		System.out.println();
		System.out.println();
		System.out.println("example of a frame");
		Frame frame = new Frame(200);
		System.out.println("type: "+ frame.type);
		System.out.println("# trials: "+ frame.numTrials);
		System.out.println("ground truth:");
		printArr(frame.groundTruth);
		System.out.println("prob:");
		printArr(frame.probVector.get(0));
		System.out.println("histogram:");
		printArr(frame.histogram.get(0));
		System.out.println("predicted:");
		System.out.println(frame.predicted);
		printArr(frame.predicted);
		*/
		for(int i = 1; i < 11;i ++) {
			System.out.println("==============================================");
			//Frame frame0 = new Frame(200, 2);
			int numTrials = i*20;
			Frame frame0 = new Frame(numTrials, 2, 0); //Frame(int numTrials, int type, int center)
			System.out.println(String.format("#trials = %d, #type = %d", numTrials, 2));
			frame0.predict(1);
			System.out.println("1 distance: "+ frame0.distance());
			frame0.predict(2);
			System.out.println("2 distance: "+ frame0.distance());
			frame0.predict(3);
			System.out.println("3 distance: "+ frame0.distance());
			frame0.predict(4);
			System.out.println("4 distance: "+ frame0.distance());
			frame0.predict(5);
			System.out.println("5 distance: "+ frame0.distance());
			frame0.predictVoted();
			System.out.println("voted distance: "+ frame0.distance());
			System.out.println("ground truth:");
			printArr(frame0.groundTruth);
			System.out.println("voted prediction");
			printArr(frame0.predicted);
			int[][] corrected = frame0.correct_convex_avg();
			System.out.println("corrected by avg and spation correlation");
			printArr(corrected);
			System.out.println("corrected distance: "+ frame0.distance(frame0.groundTruth, corrected));
		}
	}
	
	static int[][] genGroundTruth(int type){
		int[][] arr = new int[4][4];
		Random rand = new Random();
		if(type == 0) {
			for(int i = 0; i < arr.length;i ++) {
				for(int j = 0; j < arr[i].length;j ++) {
					arr[i][j] = rand.nextInt(255);
				}
			}
		}else if(type == 1) {
			int n0 = rand.nextInt(255);
			int n1 = rand.nextInt(255);
			int n2 = rand.nextInt(255);
			int n3 = rand.nextInt(255);
			arr[0][0] = n0;
			arr[1][0] = n0;
			arr[0][1] = n0;
			arr[1][1] = n0;
			
			arr[0][2] = n1;
			arr[0][3] = n1;
			arr[1][2] = n1;
			arr[1][3] = n1;
			
			arr[2][0] = n2;
			arr[3][0] = n2;
			arr[2][1] = n2;
			arr[3][1] = n2;
			
			arr[2][2] = n3;
			arr[3][2] = n3;
			arr[2][3] = n3;
			arr[3][3] = n3;
		}else if(type == 2) {	//convex
			int ci = rand.nextInt(4);
			int cj = rand.nextInt(4);
			int val = rand.nextInt(255);
			for(int i = 0; i < arr.length;i ++) {
				for(int j = 0; j < arr[i].length;j ++) {
					if(ci == i && cj == j) {
						arr[i][j] = val;
					}else if(Math.abs(ci-i) < 2 && Math.abs(cj-j) < 2) {
						arr[i][j] = val + 5;
					}else if(Math.abs(ci-i) < 3 && Math.abs(cj-j) < 3) {
						arr[i][j] = val + 10;
					}else if(Math.abs(ci-i) < 4 && Math.abs(cj-j) < 4) {
						arr[i][j] = val + 15;
					}
				}
			}
		}else if(type == 3) {//concave
			int ci = rand.nextInt(4);
			int cj = rand.nextInt(4);
			int val = rand.nextInt(255);
			for(int i = 0; i < arr.length;i ++) {
				for(int j = 0; j < arr[i].length;j ++) {
					if(ci == i && cj == j) {
						arr[i][j] = val;
					}else if(Math.abs(ci-i) < 2 && Math.abs(cj-j) < 2) {
						arr[i][j] = val - 5;
					}else if(Math.abs(ci-i) < 3 && Math.abs(cj-j) < 3) {
						arr[i][j] = val - 10;
					}else if(Math.abs(ci-i) < 4 && Math.abs(cj-j) < 4) {
						arr[i][j] = val - 15;
					}
				}
			}
		}
		return arr;
	}
	
	static double[] distance2Prob(int distance, int type) {
		double[] prob = new double[255];
		for(int i = 0; i < prob.length;i ++) {
			prob[i] = 0.3;
		}
		if(type == 0 || type == 1) {
			prob[distance-1] = prob[distance-1] + 0.3;
			prob[distance-1+1] = prob[distance-1+1] + 0;
			prob[distance-1+2] = prob[distance-1+2] + 0.3;
			prob[distance-1+3] = prob[distance-1+3] + 0;
			prob[distance-1+4] = prob[distance-1+4] + 0.3;
		}else if(type == 2 || type == 3) {
			prob[distance-1] = prob[distance-1] + 0.1;
			prob[distance-1+1] = prob[distance-1+1] + 0.4;
			prob[distance-1+2] = prob[distance-1+2] + 0.3;
			prob[distance-1+3] = prob[distance-1+3] + 0.2;
			prob[distance-1+4] = prob[distance-1+4] + 0.1;
		}
		return prob;
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
			System.out.print(arr[i] + " ");
		}
	}
	
	static void printArr(int[] arr) {
		for(int i = 0; i < arr.length;i ++) {
			System.out.print(arr[i] + "  ");
		}
	}
}
