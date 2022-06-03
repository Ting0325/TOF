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
	
	Frame(int numTrials, int type){
		Random rand = new Random();
		this.type = type;
		this.numTrials = numTrials;	//4 ~ 255
		this.groundTruth = genGroundTruth(type);
		this.probVector = genProbVector();
		this.stopVector = genStopVector();
		this.histogram = genHistogram();
		this.predicted = predict();
	}
	
	Frame(int numTrials, int type, int center){
		Random rand = new Random();
		this.type = type;
		this.numTrials = numTrials;	//4 ~ 255
		this.groundTruth = genGroundTruth(type, center);
		this.probVector = genProbVector();
		this.stopVector = genStopVector();
		this.histogram = genHistogram();
		//this.predicted = predict();
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
		
	int[][] predict(int winSize){
		int[][] dist = new int[4][4];
		if(type == 2 || type== 3) {
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//window size == 1
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					//window size == 2
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					//window size == 3
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					//window size == 4
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					//window size == 5
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3]+ histogram.get(i*4+j)[k+4];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}		
					if(winSize == 1) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 2) {
						dist[i][j] = maxIdx2 ; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 3) {
						dist[i][j] = maxIdx3 ; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(winSize == 5) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}			
				}
			}
		}else {	//type == 0 || type ==1
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//window size == 1
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					//window size == 2
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					//window size == 3
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+4];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					//window size == 4
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					//window size == 5
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3]+ histogram.get(i*4+j)[k+4];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}		
					if(winSize == 1) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 2) {
						dist[i][j] = maxIdx2 ; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 3) {
						dist[i][j] = maxIdx3 ; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(winSize == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(winSize == 5) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}			
				}
			}
		}
		
		this.predicted = dist;
		return dist;
	}
	
	int[][] predictVoted(){
		//find max for window size = 4
		int[][] dist = new int[4][4];
		if(type == 2 || type== 3) {
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}
					//voting
					int votes1 = 0, votes2 = 0, votes3 = 0, votes4 = 0, votes5 = 0;
					//votes1
					if(maxIdx1 == maxIdx2 )
						votes1 ++;
					if(maxIdx1 == maxIdx3 )
						votes1 ++;
					if(maxIdx1 == maxIdx4 +1)
						votes1 ++;
					if(maxIdx1 == maxIdx5 +1)
						votes1 ++;
					//votes2
					if(maxIdx2  == maxIdx1)
						votes2 ++;
					if(maxIdx2 == maxIdx3 )
						votes2 ++;
					if(maxIdx2  == maxIdx4 +1)
						votes2 ++;
					if(maxIdx2  == maxIdx5 +1)
						votes2 ++;
					//votes3
					if(maxIdx3  == maxIdx1)
						votes3 ++;
					if(maxIdx3  == maxIdx2 )
						votes3 ++;
					if(maxIdx3  == maxIdx4 +1)
						votes3 ++;
					if(maxIdx3  == maxIdx5 +1)
						votes3 ++;
					//votes4
					if(maxIdx4 +1 == maxIdx2)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx3 )
						votes4 ++;
					if(maxIdx4 +1 == maxIdx1)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx5 +1)
						votes4 ++;
					//votes5
					if(maxIdx5 +1 == maxIdx2 )
						votes5 ++;
					if(maxIdx5 +1 == maxIdx3)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx1)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx4 +1)
						votes5 ++;
					//analyze votes
					int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;
					//votes1
					if(votes1 >= votes2)
						cnt1 ++;
					if(votes1 >= votes3)
						cnt1 ++;
					if(votes1 >= votes4)
						cnt1 ++;
					if(votes1 >= votes5)
						cnt1 ++;
					//votes2
					if(votes2 > votes1)
						cnt2 ++;
					if(votes2 >= votes3)
						cnt2 ++;
					if(votes2 >= votes4)
						cnt2 ++;
					if(votes2 >= votes5)
						cnt2 ++;
					//votes3
					if(votes3 > votes2)
						cnt3 ++;
					if(votes3 > votes1)
						cnt3 ++;
					if(votes3 >= votes4)
						cnt3 ++;
					if(votes3 >= votes5)
						cnt3 ++;
					//votes4
					if(votes4 > votes2)
						cnt4 ++;
					if(votes4 > votes1)
						cnt4 ++;
					if(votes4 > votes3)
						cnt4 ++;
					if(votes4 >= votes5)
						cnt4 ++;
					//votes5
					if(votes5 > votes2)
						cnt5 ++;
					if(votes5 > votes1)
						cnt5 ++;
					if(votes5 > votes3)
						cnt5 ++;
					if(votes5 >= votes4)
						cnt5 ++;
					
					if(cnt1 == 4) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(cnt2 == 4) {
						dist[i][j] = maxIdx2 +1; //distance starts at 1
					}else if(cnt3 == 4) {
						dist[i][j] = maxIdx3 +1; //distance starts at 1
					}else if(cnt4 == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(cnt5 == 4) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}
				}
			}
		}else {	//type ==0 || type==1
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+4];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}
					//voting
					int votes1 = 0, votes2 = 0, votes3 = 0, votes4 = 0, votes5 = 0;
					//votes1
					if(maxIdx1 == maxIdx2 )
						votes1 ++;
					if(maxIdx1 == maxIdx3 )
						votes1 ++;
					if(maxIdx1 == maxIdx4 +1)
						votes1 ++;
					if(maxIdx1 == maxIdx5 +1)
						votes1 ++;
					//votes2
					if(maxIdx2  == maxIdx1)
						votes2 ++;
					if(maxIdx2 == maxIdx3 )
						votes2 ++;
					if(maxIdx2  == maxIdx4 +1)
						votes2 ++;
					if(maxIdx2  == maxIdx5 +1)
						votes2 ++;
					//votes3
					if(maxIdx3  == maxIdx1)
						votes3 ++;
					if(maxIdx3  == maxIdx2 )
						votes3 ++;
					if(maxIdx3  == maxIdx4 +1)
						votes3 ++;
					if(maxIdx3  == maxIdx5 +1)
						votes3 ++;
					//votes4
					if(maxIdx4 +1 == maxIdx2)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx3 )
						votes4 ++;
					if(maxIdx4 +1 == maxIdx1)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx5 +1)
						votes4 ++;
					//votes5
					if(maxIdx5 +1 == maxIdx2 )
						votes5 ++;
					if(maxIdx5 +1 == maxIdx3)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx1)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx4 +1)
						votes5 ++;
					//analyze votes
					int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;
					//votes1
					if(votes1 >= votes2)
						cnt1 ++;
					if(votes1 >= votes3)
						cnt1 ++;
					if(votes1 >= votes4)
						cnt1 ++;
					if(votes1 >= votes5)
						cnt1 ++;
					//votes2
					if(votes2 > votes1)
						cnt2 ++;
					if(votes2 >= votes3)
						cnt2 ++;
					if(votes2 >= votes4)
						cnt2 ++;
					if(votes2 >= votes5)
						cnt2 ++;
					//votes3
					if(votes3 > votes2)
						cnt3 ++;
					if(votes3 > votes1)
						cnt3 ++;
					if(votes3 >= votes4)
						cnt3 ++;
					if(votes3 >= votes5)
						cnt3 ++;
					//votes4
					if(votes4 > votes2)
						cnt4 ++;
					if(votes4 > votes1)
						cnt4 ++;
					if(votes4 > votes3)
						cnt4 ++;
					if(votes4 >= votes5)
						cnt4 ++;
					//votes5
					if(votes5 > votes2)
						cnt5 ++;
					if(votes5 > votes1)
						cnt5 ++;
					if(votes5 > votes3)
						cnt5 ++;
					if(votes5 >= votes4)
						cnt5 ++;
					
					if(cnt1 == 4) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(cnt2 == 4) {
						dist[i][j] = maxIdx2 +1; //distance starts at 1
					}else if(cnt3 == 4) {
						dist[i][j] = maxIdx3 +1; //distance starts at 1
					}else if(cnt4 == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(cnt5 == 4) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}
				}
			}
		}

		this.predicted = dist;
		return dist;
	}
	
	int[][] predictVoted_v2(){
		//find max for window size = 4
		int[][] dist = new int[4][4];
		if(type == 2 || type== 3) {
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}
					maxIdx1 = maxIdx3;	//hack
					//voting
					int votes1 = 0, votes2 = 0, votes3 = 0, votes4 = 0, votes5 = 0;
					//votes1
					if(maxIdx1 == maxIdx2 )
						votes1 ++;
					if(maxIdx1 == maxIdx3 )
						votes1 ++;
					if(maxIdx1 == maxIdx4 +1)
						votes1 ++;
					if(maxIdx1 == maxIdx5 +1)
						votes1 ++;
					//votes2
					if(maxIdx2  == maxIdx1)
						votes2 ++;
					if(maxIdx2 == maxIdx3 )
						votes2 ++;
					if(maxIdx2  == maxIdx4 +1)
						votes2 ++;
					if(maxIdx2  == maxIdx5 +1)
						votes2 ++;
					//votes3
					if(maxIdx3  == maxIdx1)
						votes3 ++;
					if(maxIdx3  == maxIdx2 )
						votes3 ++;
					if(maxIdx3  == maxIdx4 +1)
						votes3 ++;
					if(maxIdx3  == maxIdx5 +1)
						votes3 ++;
					//votes4
					if(maxIdx4 +1 == maxIdx2)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx3 )
						votes4 ++;
					if(maxIdx4 +1 == maxIdx1)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx5 +1)
						votes4 ++;
					//votes5
					if(maxIdx5 +1 == maxIdx2 )
						votes5 ++;
					if(maxIdx5 +1 == maxIdx3)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx1)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx4 +1)
						votes5 ++;
					//analyze votes
					int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;
					//votes1
					if(votes1 >= votes2)
						cnt1 ++;
					if(votes1 >= votes3)
						cnt1 ++;
					if(votes1 >= votes4)
						cnt1 ++;
					if(votes1 >= votes5)
						cnt1 ++;
					//votes2
					if(votes2 > votes1)
						cnt2 ++;
					if(votes2 >= votes3)
						cnt2 ++;
					if(votes2 >= votes4)
						cnt2 ++;
					if(votes2 >= votes5)
						cnt2 ++;
					//votes3
					if(votes3 > votes2)
						cnt3 ++;
					if(votes3 > votes1)
						cnt3 ++;
					if(votes3 >= votes4)
						cnt3 ++;
					if(votes3 >= votes5)
						cnt3 ++;
					//votes4
					if(votes4 > votes2)
						cnt4 ++;
					if(votes4 > votes1)
						cnt4 ++;
					if(votes4 > votes3)
						cnt4 ++;
					if(votes4 >= votes5)
						cnt4 ++;
					//votes5
					if(votes5 > votes2)
						cnt5 ++;
					if(votes5 > votes1)
						cnt5 ++;
					if(votes5 > votes3)
						cnt5 ++;
					if(votes5 >= votes4)
						cnt5 ++;
					
					if(cnt1 == 4) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(cnt2 == 4) {
						dist[i][j] = maxIdx2 +1; //distance starts at 1
					}else if(cnt3 == 4) {
						dist[i][j] = maxIdx3 +1; //distance starts at 1
					}else if(cnt4 == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(cnt5 == 4) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}
				}
			}
		}else {	//type ==0 || type==1
			for(int i = 0; i < 4;i ++) {
				for(int j = 0; j < 4;j ++) {
					//
					int max1 = 0;
					int maxIdx1 = 0;
					for(int k = 0; k < 254;k ++) {
						int sum1 = histogram.get(i*4+j)[k];
						if(sum1 > max1) {
							max1 = sum1;
							maxIdx1 = k;
						}
					}
					int max2 = 0;
					int maxIdx2 = 0;
					for(int k = 0; k < 253;k ++) {
						int sum2 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2];
						if(sum2 > max2) {
							max2 = sum2;
							maxIdx2 = k;
						}
					}
					int max3 = 0;
					int maxIdx3 = 0;
					for(int k = 0; k < 252;k ++) {
						int sum3 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+4];
						if(sum3 > max3) {
							max3 = sum3;
							maxIdx3 = k;
						}
					}
					int max4 = 0;
					int maxIdx4 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum4 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum4 > max4) {
							max4 = sum4;
							maxIdx4 = k;
						}
					}
					int max5 = 0;
					int maxIdx5 = 0;
					for(int k = 0; k < 251;k ++) {
						int sum5 = histogram.get(i*4+j)[k] + histogram.get(i*4+j)[k+1] + histogram.get(i*4+j)[k+2] + histogram.get(i*4+j)[k+3];
						if(sum5 > max5) {
							max5 = sum5;
							maxIdx5 = k;
						}
					}
					//voting
					int votes1 = 0, votes2 = 0, votes3 = 0, votes4 = 0, votes5 = 0;
					//votes1
					if(maxIdx1 == maxIdx2 )
						votes1 ++;
					if(maxIdx1 == maxIdx3 )
						votes1 ++;
					if(maxIdx1 == maxIdx4 +1)
						votes1 ++;
					if(maxIdx1 == maxIdx5 +1)
						votes1 ++;
					//votes2
					if(maxIdx2  == maxIdx1)
						votes2 ++;
					if(maxIdx2 == maxIdx3 )
						votes2 ++;
					if(maxIdx2  == maxIdx4 +1)
						votes2 ++;
					if(maxIdx2  == maxIdx5 +1)
						votes2 ++;
					//votes3
					if(maxIdx3  == maxIdx1)
						votes3 ++;
					if(maxIdx3  == maxIdx2 )
						votes3 ++;
					if(maxIdx3  == maxIdx4 +1)
						votes3 ++;
					if(maxIdx3  == maxIdx5 +1)
						votes3 ++;
					//votes4
					if(maxIdx4 +1 == maxIdx2)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx3 )
						votes4 ++;
					if(maxIdx4 +1 == maxIdx1)
						votes4 ++;
					if(maxIdx4 +1 == maxIdx5 +1)
						votes4 ++;
					//votes5
					if(maxIdx5 +1 == maxIdx2 )
						votes5 ++;
					if(maxIdx5 +1 == maxIdx3)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx1)
						votes5 ++;
					if(maxIdx5 +1 == maxIdx4 +1)
						votes5 ++;
					//analyze votes
					int cnt1 = 0, cnt2 = 0, cnt3 = 0, cnt4 = 0, cnt5 = 0;
					//votes1
					if(votes1 >= votes2)
						cnt1 ++;
					if(votes1 >= votes3)
						cnt1 ++;
					if(votes1 >= votes4)
						cnt1 ++;
					if(votes1 >= votes5)
						cnt1 ++;
					//votes2
					if(votes2 > votes1)
						cnt2 ++;
					if(votes2 >= votes3)
						cnt2 ++;
					if(votes2 >= votes4)
						cnt2 ++;
					if(votes2 >= votes5)
						cnt2 ++;
					//votes3
					if(votes3 > votes2)
						cnt3 ++;
					if(votes3 > votes1)
						cnt3 ++;
					if(votes3 >= votes4)
						cnt3 ++;
					if(votes3 >= votes5)
						cnt3 ++;
					//votes4
					if(votes4 > votes2)
						cnt4 ++;
					if(votes4 > votes1)
						cnt4 ++;
					if(votes4 > votes3)
						cnt4 ++;
					if(votes4 >= votes5)
						cnt4 ++;
					//votes5
					if(votes5 > votes2)
						cnt5 ++;
					if(votes5 > votes1)
						cnt5 ++;
					if(votes5 > votes3)
						cnt5 ++;
					if(votes5 >= votes4)
						cnt5 ++;
					
					if(cnt1 == 4) {
						dist[i][j] = maxIdx1; //distance starts at 1 (for type2 and type3 the second idx has th largest probibility)
					}else if(cnt2 == 4) {
						dist[i][j] = maxIdx2 +1; //distance starts at 1
					}else if(cnt3 == 4) {
						dist[i][j] = maxIdx3 +1; //distance starts at 1
					}else if(cnt4 == 4) {
						dist[i][j] = maxIdx4 +1; //distance starts at 1
					}else if(cnt5 == 4) {
						dist[i][j] = maxIdx5 +1; //distance starts at 1
					}
				}
			}
		}

		this.predicted = dist;
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
