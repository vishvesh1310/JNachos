package jnachos.kern;

public class Banker
{
	int nP,nR;
	int[] tempAvai;
	
	
    public void initialize()
    {
        nP=NachosProcess.numberOfProcess();
        nR=JNachos.numberOfResource();
        tempAvai=new int[nR];
        
        for(int i=0;i<nR;i++)
        {
        	tempAvai[i]=JNachos.BankerAvai[i];
        }
        
        for(int i=0;i<nP;i++)
        {
            for(int j=0;j<nR;j++)  //calculating need matrix
            {
                JNachos.BankerNeed[i][j]=JNachos.BankerMAX[i][j]-JNachos.BankerAlloc[i][j];
            }
        }
         
    }

    private boolean check(int i)
    {
       //checking if all resources for ith process can be allocated
       for(int j=0;j<nR;j++) 
       {
               if(JNachos.BankerAvai[j]<JNachos.BankerNeed[i][j])
               {
                  return false;
               }
       }
       return true;
    }
 
    public void isSafe()
    {
        initialize();
        boolean done[]=new boolean[nP];
        
        int j=0;
 
        while(j<nP) //until all process allocated
        {  
               boolean allocated=false;
               for(int i=0;i<nP;i++)
               {
            	  int index=JNachos.processIdList.get(i);
            	  if(!done[i] && check(index))
	              {  
	                	//trying to allocate
	                    for(int k=0;k<nR;k++)
	                    	tempAvai[k]=tempAvai[k]+JNachos.BankerAlloc[index][k];
	                    System.out.println("Allocated process : "+i);
	                    allocated=done[i]=true;
	                    j++;
	              }
                  if(!allocated) 
                      break;  //if no allocation
              }
        }
        if(j==nP)  //if all processes are allocated
               System.out.println("\nSafely allocated");
        else
            System.out.println("All proceess cant be allocated safely");
    }
}