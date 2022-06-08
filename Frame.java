import java.util.ArrayList;
import java.util.Random;

public class Frame {
	int type;
	int numTrials;
	int[][] groundTruth;
	int[][] predicted;
	int[][][] predicted_top3;
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
	
	Frame(int numTrials, int type){
		Random rand = new Random();
		this.type = type;
		this.numTrials = numTrials;	//4 ~ 255
		this.groundTruth = genGroundTruth(type);
		this.probVector = genProbVector();
		this.stopVector = genStopVector();
		this.histogram = genHistogram();
		//this.predicted = predict();
	}
	
	Frame(int numTrials, int type, int center){
		Random rand = new Random();
		this.type = type;
		this.numTrials = numTrials;	//4 ~ 255
		this.groundTruth = genGroundTruth(type, center);
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
					arr[i][j] = rand.nextInt(251)+1;
				}
			}
		}else if(type == 1) {
			int n0 = rand.nextInt(251)+1;
			int n1 = rand.nextInt(251)+1;
			int n2 = rand.nextInt(251)+1;
			int n3 = rand.nextInt(251)+1;
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
			int val = rand.nextInt(236)+1;
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
	
	int[][] genGroundTruth(int type, int center){
		int[][] arr = new int[4][4];
		Random rand = new Random();
		if(type == 0) {
			for(int i = 0; i < arr.length;i ++) {
				for(int j = 0; j < arr[i].length;j ++) {
					arr[i][j] = rand.nextInt(255);
				}
			}
		}else if(type == 1) {
			int n0 = rand.nextInt(254)+1;
			int n1 = rand.nextInt(254)+1;
			int n2 = rand.nextInt(254)+1;
			int n3 = rand.nextInt(254)+1;
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
			int ci = center/4;//rand.nextInt(4);
			int cj = center%4;//rand.nextInt(4);
			int val = rand.nextInt(236)+1;
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
					//int sum = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
					int sum = histogram.get(i*4+j)[k]*4 + histogram.get(i*4+j)[k+1]*7 + histogram.get(i*4+j)[k+2]*6 + histogram.get(i*4+j)[k+3]*5+ histogram.get(i*4+j)[k+4]*4;
					if(sum > max) {
						max = sum;
						maxIdx = k;
					}
				}
				dist[i][j] = maxIdx +1; //distance starts at 1
				
			}
		}
		predicted = dist;
		return dist;
	}
		
	int[][][] predict(int winSize){
		int[][][] dist = new int[16][4][4];
		
		if(type == 2 || type== 3) {
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//window size == 5
					int max5_1 = 0;
					int maxIdx5_1 = 0;
					int max5_2 = 0;
					int maxIdx5_2 = 0;
					int max5_3 = 0;
					int maxIdx5_3 = 0;
					int max5_4 = 0;
					int maxIdx5_4 = 0;
					int max5_5 = 0;
					int maxIdx5_5 = 0;
					int max5_6 = 0;
					int maxIdx5_6 = 0;
					int max5_7 = 0;
					int maxIdx5_7 = 0;
					int max5_8 = 0;
					int maxIdx5_8 = 0;
					int max5_9 = 0;
					int maxIdx5_9 = 0;
					int max5_10 = 0;
					int maxIdx5_10 = 0;
					int max5_11 = 0;
					int maxIdx5_11 = 0;
					int max5_12 = 0;
					int maxIdx5_12 = 0;
					int max5_13 = 0;
					int maxIdx5_13 = 0;
					int max5_14 = 0;
					int maxIdx5_14 = 0;
					int max5_15 = 0;
					int maxIdx5_15 = 0;
					int max5_16 = 0;
					int maxIdx5_16 = 0;
					for(int k = 0; k < 251;k ++) {
						//int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3]+ histogram.get(i*4+j)[k+4];
						int sum5 = histogram.get(i*4+j)[k]*4 + histogram.get(i*4+j)[k+1]*7 + histogram.get(i*4+j)[k+2]*6 + histogram.get(i*4+j)[k+3]*5+ histogram.get(i*4+j)[k+4]*4;
						if(sum5 > max5_1) {
							max5_1 = sum5;
							maxIdx5_1 = k;
						}else if(sum5 > max5_2) {
							max5_2 = sum5;
							maxIdx5_2 = k;
						}else if(sum5 > max5_3) {
							max5_3 = sum5;
							maxIdx5_3 = k;
						}else if(sum5 > max5_4) {
							max5_4 = sum5;
							maxIdx5_4 = k;
						}else if(sum5 > max5_5) {
							max5_5 = sum5;
							maxIdx5_5 = k;
						}else if(sum5 > max5_6) {
							max5_6 = sum5;
							maxIdx5_6 = k;
						}else if(sum5 > max5_7) {
							max5_7 = sum5;
							maxIdx5_7 = k;
						}else if(sum5 > max5_8) {
							max5_8 = sum5;
							maxIdx5_8 = k;
						}else if(sum5 > max5_9) {
							max5_9 = sum5;
							maxIdx5_9 = k;
						}else if(sum5 > max5_10) {
							max5_10 = sum5;
							maxIdx5_10 = k;
						}else if(sum5 > max5_11) {
							max5_11 = sum5;
							maxIdx5_11 = k;
						}else if(sum5 > max5_12) {
							max5_12 = sum5;
							maxIdx5_12 = k;
						}else if(sum5 > max5_13) {
							max5_13 = sum5;
							maxIdx5_13 = k;
						}else if(sum5 > max5_14) {
							max5_14 = sum5;
							maxIdx5_14 = k;
						}else if(sum5 > max5_15) {
							max5_15 = sum5;
							maxIdx5_15 = k;
						}else if(sum5 > max5_16) {
							max5_16 = sum5;
							maxIdx5_16 = k;
						}
					}		
					dist[0][i][j] = maxIdx5_1 +1; //distance starts at 1			
					dist[1][i][j] = maxIdx5_2 +1; //distance starts at 1	
					dist[2][i][j] = maxIdx5_3 +1; //distance starts at 1	
					dist[3][i][j] = maxIdx5_4 +1; //distance starts at 1	
					dist[4][i][j] = maxIdx5_5 +1; //distance starts at 1
					dist[5][i][j] = maxIdx5_6 +1; //distance starts at 1
					dist[6][i][j] = maxIdx5_7 +1; //distance starts at 1
					dist[7][i][j] = maxIdx5_8 +1; //distance starts at 1
					dist[8][i][j] = maxIdx5_9 +1; //distance starts at 1
					dist[9][i][j] = maxIdx5_10 +1; //distance starts at 1
					dist[10][i][j] = maxIdx5_11 +1; //distance starts at 1
					dist[11][i][j] = maxIdx5_12 +1; //distance starts at 1
					dist[12][i][j] = maxIdx5_13 +1; //distance starts at 1
					dist[13][i][j] = maxIdx5_14 +1; //distance starts at 1
					dist[14][i][j] = maxIdx5_15 +1; //distance starts at 1
					dist[15][i][j] = maxIdx5_16 +1; //distance starts at 1
				}
			}
		
		}
		this.predicted_top3 = dist;
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
	
	int distance(int[][]a, int[][]b) {
		int d = 0;
		for(int i = 0; i < groundTruth.length;i ++) {
			for(int j = 0; j < groundTruth[i].length;j ++) {
				d += Math.abs(a[i][j] - b[i][j]);
			}
		}
		return d;
	}
	
	int[][] correct_convex_avg(){	//type 2
		int[][] corrected = new int[4][4];
		int min = 0, min_i = 0, min_j = 0, min_idx =0;
		for(int i = 0; i < groundTruth.length;i ++) {
			for(int j = 0; j < groundTruth[i].length;j ++) {
				if(groundTruth[i][j] < min) {
					min = groundTruth[i][j];
					min_i = i;
					min_j = j;
					min_idx = i*4+j;
				}
			}
		}
		double sum = 0;
		int avg = 0;
		for(int i = 0; i < predicted.length;i ++) {
			for(int j = 0; j < predicted[i].length;j ++) {
				sum += predicted[i][j];
			}
		}
		System.out.println("min_idx:"+min_idx);
		switch(min_idx) {
			case 0:	//sum = predicted[0][3]+predicted[1][3]+predicted[2][3]+predicted[3][3]+predicted[3][2]+predicted[3][2]+predicted[3][1]+predicted[3][0];
					avg = (int) Math.round((sum-170)/16);
					corrected[0][0] = avg;
					corrected[0][1] = avg+5;
					corrected[0][2] = avg+10;
					corrected[0][3] = avg+15;
					corrected[1][0] = avg+5;
					corrected[1][1] = avg+5;
					corrected[1][2] = avg+10;
					corrected[1][3] = avg+15;
					corrected[2][0] = avg+10;
					corrected[2][1] = avg+10;
					corrected[2][2] = avg+10;
					corrected[2][3] = avg+15;
					corrected[3][0] = avg+15;
					corrected[3][1] = avg+15;
					corrected[3][2] = avg+15;
					corrected[3][3] = avg+15;
		}
		

		
		return corrected;
		
		
	}
}
