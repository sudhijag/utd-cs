#include "simos.h"

//=====================================
// system.c reads in system wide parameters from "config.sys"
// and calls the intialization functions in each module
//=====================================

void configure_system ()
{ FILE *fconfig;
  char str[60];

  fconfig = fopen ("config.sys", "r");
  fscanf (fconfig, "%d %d %d %s\n",
          &maxProcess, &cpuQuantum, &idleQuantum, str);
  fscanf (fconfig, "%d %d %s\n", &pageSize, &numFrames, str);
  fscanf (fconfig, "%d %d %d %s\n", &loadPpages, &maxPpages, &OSpages, str);
  fscanf (fconfig, "%d %d %d %d %s\n",
          &agescanPeriod, &instrTime, &termPrintTime, &diskRWtime, str);
  fscanf (fconfig, "%d %d %d %d %d %d %s\n",
          &cpuDebug, &memDebug, &termDebug, &swapDebug, &clockDebug, 
          &uiDebug, str);
  fclose (fconfig);

  bugF = fopen ("debug.tmp", "w");
	
  //FILE *inf = fdopen(parentfd[1], "w");
  infF = stdout;
  // bugF = stderr;
}

void initialize_system ()
{
  printf("INIT SYSTEM");
  configure_system ();

  //========== initialize the data structures in the main thread
  initialize_timer ();
  initialize_cpu ();
  initialize_physical_memory ();  // 3 memory initialization
  initialize_mframe_manager ();
  initialize_agescan ();
  initialize_process_manager ();

  //========== start the other two threads
  start_terminal ();   // term.c
  start_swap_manager ();   // swap.c
  printf("DONE INIT SYSTEM");
}

void system_exit ()
{
  // wait for the other threads to clean up and terminate
  end_terminal ();
  end_swap_manager ();

  fclose (bugF);
}

//not called
void old_main ()
{ char action[10];

  systemActive = 1;
  // swap and term threads have a while loop controlled by systemActive
  // admin's while loop is also controlled by systemActive
  // admin's T command sets systemActive to 0, stop all threads

  initialize_system ();
  process_admin_commands ();

  fprintf (infF, "System exiting!!!\n");
  system_exit ();
}

