package Gene_Prediction;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Gene_Prediction {
    private double[][] transition;
    private double[][] G_emission;
    private int[][] state;
    private int max_size = 20000,previous=0;
    private double N_emission=0.25;
	private double[][] probility;
    Gene_Prediction(){
    	// col is A,C,G,T
    	G_emission=new double[][] {
    		//A with ACGT
    		{77,66,37,4},
    		{3,63,13,19},
    		{1,23,1,3},
    		{1,98,60,29},
    		//C with ACGT
    		{15,23,73,5},
    		{11,1,55,9},
    		{1,46,1,89},
    		{1,18,141,11},
    		//G with ACGT
    		{147,85,46,60},
    		{30,19,49,30},
    		{1,47,5,78},
    		{34,21,34,55},
    		//T with ACGT
    		{0,38,0,18},
    		{2,38,5,32},
    		{0,5,8,5},
    		{2,44,8,15}
    	};
    	Scanner sc=new Scanner(System.in);
    	System.out.println("please input tansition posibility");
    	setTransition(sc.nextDouble(),sc.nextDouble());
//    	setTransition(0.6,0.8);
    	//starts in state N with probability 1.
    	System.out.println("input file directionary");
    	FileReader fr;
    	String fileName= sc.next();
    	state=new int[max_size][2];
    	state[previous][0]=0;
    	probility = new double[max_size][2];
    	probility[previous][0]=1;
    	probility[previous][1]=0;
    	try {
			fr = new FileReader(fileName);
			int tempChar=-1;  
			int n=0;
			String tem="";
	        while((tempChar=fr.read())!=-1){
	        	if((char)tempChar!='A'&&(char)tempChar!='C'&&(char)tempChar!='G'&&(char)tempChar!='T')
	        		continue;
	        	n++;
	        	tem+=(char)tempChar;
	        	if(n%3==0) {
	        		this.evaluate(tem);
	        		n=0;
	        		tem="";
	        	}
	        }  
	        fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    	display();
    }
    void display() {
    	System.out.println(probility[previous][0]+" "+probility[previous][1]);
    	double p=probility[previous][0]>probility[previous][1]?probility[previous][0]:probility[previous][1];
    	System.out.println("joint p is:"+p);
    	int s=probility[previous][0]>probility[previous][1]?0:1;
    	String sequence="";
    	System.out.println(previous);
    	for(;previous>0;previous--)
    		{
    		if(s==0)
    		    sequence+="N";
    		else
    			sequence+="G";
    		s=state[previous][s];
    		}
    	try {
			FileWriter writer=new FileWriter("D:\\output.txt");
			writer.write(new StringBuffer(sequence).reverse().toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    void setTransition(double pnn,double pgg) {
    	transition=new double[][] {
    		{pnn,1-pnn},
    		{1-pgg,pgg},
    	};
    }
    void evaluate(String trim) {
    	double tem[]=new double[4];
    	tem[0]=probility[previous][0]*transition[0][0]*N_emission;
    	tem[1]=probility[previous][1]*transition[1][0]*N_emission;
    	tem[2]=probility[previous][0]*transition[0][1]*this.get_ppsibility(trim);
    	tem[3]=probility[previous][1]*transition[1][1]*this.get_ppsibility(trim);
    	previous++;
    	state[previous][0]=tem[0]>tem[1]?0:1;
    	state[previous][1]=tem[2]>tem[3]?0:1;
    	probility[previous][0]=tem[0]>tem[1]?tem[0]:tem[1];
    	probility[previous][1]=tem[2]>tem[3]?tem[2]:tem[3];
    	if(probility[previous][0]<1e-5||probility[previous][1]<1e-5) {
    		probility[previous][0]*=10000;
    		probility[previous][1]*=10000;
    	}
//    	System.out.println(probility[previous][0]+";"+probility[previous][1]);
    }
    double get_ppsibility(String tri) {
    	char[] tem=tri.toCharArray();
    	int x=0;
        x=recognize(tem[0])*4+recognize(tem[1]);
        int count=0;
        for(int n=0;n<4;n++)
        	count+=this.G_emission[x][n];
        return this.G_emission[x][recognize(tem[2])]/count;
    }
    int recognize(char gene) {
    	switch(gene){
    	case 'A':
    		return 0;
    	case 'C':
    		return 1;
    	case 'G':
    		return 2;
    	default:
    		return 3;
    	}
    }
    public static void main(String args[]) {
    	new Gene_Prediction();
    }
}
