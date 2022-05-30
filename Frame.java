import java.util.ArrayList;
import java.util.Random;

public class Frame {
	int type;
	int numTrials;
	int[][] groundTruth;
	int[][] predicted;
	int windowSize;
	ArrayList<double[]> probVector;
	ArrayList<int[][]> stopVector;
	ArrayList<int[]> histogram;
	
	
	Frame(){
		Random rand = new Random();
		this.type = rand.nextInt(4);
		this.numTrials = rand.nextInt(221)+4;	//4 ~ 255
		this.groundTruth = genGroundTruth(type);
		this.probVector = genProbVector();
		this.stopVector = genStopVector();
		this.histogram = genHistogram();
		this.predicted = predict();
		
	}
	
	Frame(int numTrials){
		Random rand = new Random();
		this.type = rand.nextInt(4);
		this.numTrials = numTrials;	//4 ~ 255
		this.groundTruth = genGroundTruth(type);
		this.probVector = genProbVector();
		this.stopVector = genStopVector();
		this.histogram = genHistogram();
		this.predicted = predict();
	}
	ArrayList<double[]> genProbVector(){
		ArrayList<double[]> list = new ArrayList<double[]>();
		for(int i = 0;i < 16;i ++) {
			list.add(distance2Prob(groundTruth[i/4][i%4], type));
		}
		return list;
	}
	
	ArrayList<int[][]> genStopVector() {
		ArrayList<int[][]> list = new ArrayList<int[][]>();
		for(int n = 0;n < 16;n ++) {
			int[][] stop = new int[numTrials][255];
			for(int i = 0; i < numTrials; i ++) {
				for(int j = 0; j < stop[i].length;j ++) {
					stop[i][j] = (Math.random() < probVector.get(n)[j])?1:0;
				}
			}
			list.add(stop);
		}
		return list;
	}
	
	ArrayList<int[]> genHistogram() {
		ArrayList<int[]> list = new ArrayList<int[]>();
		for(int n = 0;n < 16;n ++) {
			int[] histogram = new int[255];
			for(int j = 0; j < histogram.length;j ++) {
				for(int i = 0; i < numTrials; i ++) {
					histogram[j] += stopVector.get(n)[i][j];
				}
			}
			list.add(histogram);
		}
		return list;
	}
	
	int[][] genGroundTruth(int type){
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
	
	double[] distance2Prob(int distance, int type) {
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
	
	int[][] predict(){
		//find max for window size = 4
		int[][] dist = new int[4][4];
		for(int i = 0; i < 4;i ++) {
			for(int j = 0; j < 4;j ++) {
				//
				int max = 0;
				int maxIdx = 0;
				for(int k = 0; k < 251;k ++) {
					int sum = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
					if(sum > max) {
						max = sum;
						maxIdx = k;
					}
				}
				dist[i][j] = maxIdx +1; //distance starts at 1
				
			}
		}
		return dist;
	}
	
	void printArr(int[][] arr) {
		for(int i = 0; i < arr.length;i ++) {
			for(int j = 0; j < arr[i].length;j ++) {
				System.out.print(arr[i][j]+"	");
			}
			System.out.println();
		}
	}
	
	void printArr(double[] arr) {
		for(int i = 0; i < arr.length;i ++) {
			System.out.print(arr[i] + " ");
		}
	}
	
	int distance() {
		int d = 0;
		for(int i = 0; i < groundTruth.length;i ++) {
			for(int j = 0; j < groundTruth[i].length;j ++) {
				d += Math.abs(groundTruth[i][j] - predicted[i][j]);
			}
		}
		return d;
	}
	
}
