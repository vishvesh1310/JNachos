/**
 * Copyright (c) 1992-1993 The Regents of the University of California.
 * All rights reserved.  See copyright.h for copyright notice and limitation 
 * of liability and disclaimer of warranty provisions.
 *  
 *  Created by Patrick McSweeney on 12/5/08.
 */
package jnachos.kern;

import jnachos.kern.sync.Semaphore;
import jnachos.machine.*;

/** The class handles System calls made from user programs. */
public class SystemCallHandler {
	/** The System call index for halting. */
	public static final int SC_Halt = 0;

	/** The System call index for exiting a program. */
	public static final int SC_Exit = 1;

	/** The System call index for executing program. */
	public static final int SC_Exec = 2;

	/** The System call index for joining with a process. */
	public static final int SC_Join = 3;

	/** The System call index for creating a file. */
	public static final int SC_Create = 4;

	/** The System call index for opening a file. */
	public static final int SC_Open = 5;

	/** The System call index for reading a file. */
	public static final int SC_Read = 6;

	/** The System call index for writting a file. */
	public static final int SC_Write = 7;

	/** The System call index for closing a file. */
	public static final int SC_Close = 8;

	/** The System call index for forking a forking a new process. */
	public static final int SC_Fork = 9;

	/** The System call index for yielding a program. */
	public static final int SC_Yield = 10;
	
	/** The System call index for getting semaphore. */
	public static final int SC_GetSemaphore=12;
	
	/** The System call index for P function of semaphore. */
	public static final int SC_P=13;
	
	/** The System call index for V function of semaphore. */
	public static final int SC_V=14;
	
	/** The System call index for defining max for the process. */
	public static final int SC_MaxRe=15;
	
	/**
	 * Entry point into the Nachos kernel. Called when a user program is
	 * executing, and either does a syscall, or generates an addressing or
	 * arithmetic exception.
	 * 
	 * For system calls, the following is the calling convention:
	 * 
	 * system call code -- r2 arg1 -- r4 arg2 -- r5 arg3 -- r6 arg4 -- r7
	 * 
	 * The result of the system call, if any, must be put back into r2.
	 * 
	 * And don't forget to increment the pc before returning. (Or else you'll
	 * loop making the same system call forever!
	 * 
	 * @pWhich is the kind of exception. The list of possible exceptions are in
	 *         Machine.java
	 **/
	public static void handleSystemCall(int pWhichSysCall) {

		System.out.println("SysCall:" + pWhichSysCall);

		switch (pWhichSysCall) {
		// If halt is received shut down
		case SC_Halt:
			Debug.print('a', "Shutdown, initiated by user program.");
			Interrupt.halt();
			break;

		case SC_Exit:
			// Read in any arguments from the 4th register
			int arg = Machine.readRegister(4);

			System.out
					.println("Current Process " + JNachos.getCurrentProcess().getName() + " exiting with code " + arg);
			
			JNachos.processIdRemove(JNachos.getCurrentProcess().getId());
			
			// Finish the invoking process
			JNachos.getCurrentProcess().finish();
			break;
			
		case SC_GetSemaphore:
		{
			int flag = 0;
			int semaphoreSA = Machine.readRegister(4);
			String str = new String();
			String str1 = new String();
			str="";
			str1="";

			int val =1;
			while((char)val!='\0')
			{
				val = Machine.readMem(semaphoreSA, 1);

				if((char)val == ',')
				{
					flag = 1;
				}
				if(flag == 0)
				{
					if((char)val!='\0')
						str += (char)val;
				}
				else
				{					
					if((char)val!=',')
						str1 += (char)val;
				}
				semaphoreSA++;
			}
			int count=Integer.parseInt(str1);
			JNachos.add(str,count);
			//Store return value
			break;
		}
		
		case SC_P:
		{
			int semaphoreSA = Machine.readRegister(4);
			String str = new String();
			str="";
			int val =1;
			while((char)val!='\0')
			{
				val = Machine.readMem(semaphoreSA, 1);
				if((char)val!='\0')
					str += (char)val;
				semaphoreSA++;
			}
			int semIndex=Integer.parseInt(str);
			// x is will have argument
			
			//Can Do this in Semphore class 
			//Jnachos.semObjList[x].P();
			
			JNachos.getSemObj(semIndex).P();
			
			break;
		}	
			
		case SC_V:
		{
			int semaphoreSA = Machine.readRegister(4);
			String str = new String();
			str="";
			int val =1;
			while((char)val!='\0')
			{
				val = Machine.readMem(semaphoreSA, 1);
				if((char)val!='\0')
					str += (char)val;
				semaphoreSA++;
			}
			int semIndex=Integer.parseInt(str);
			JNachos.getSemObj(semIndex).V();
			
			break;
		}
			
		
		case SC_MaxRe:
			int flag = 0;
			int semaphoreSA = Machine.readRegister(4);
			String str = new String();
			String str1 = new String();
			str="";
			str1="";

			int val =1;
			while((char)val!='\0')
			{
				val = Machine.readMem(semaphoreSA, 1);

				if((char)val == ',')
				{
					flag = 1;
				}
				if(flag == 0)
				{
					if((char)val!='\0')
						str += (char)val;
				}
				else
				{					
					if((char)val!=',')
						str1 += (char)val;
				}
				semaphoreSA++;
			}
			int index=Integer.parseInt(str);
			int count=Integer.parseInt(str1);
			int pId=JNachos.getCurrentProcess().getId();
			JNachos.setMax(pId,index,count);
			break;
			
		default:
			Interrupt.halt();
			break;
		}
	}
}
