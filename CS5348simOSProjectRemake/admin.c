#include "simos.h"


void execute_process_iteratively ()
{ int round, i;

  fprintf (infF, "Iterative execution: #rounds? ");
  scanf ("%d", &round);
  for (i=0; i<round; i++) execute_process();
}

void one_admin_command (char act){ 
  char fname[100];
  printf("In one_admin_command, commands is: %c", act);
  fflush(stdout);

  //printf("parentfd: %d" , parentfd); 
  redirect = fdopen(parentfd, "w");
  if(redirect == NULL){
	printf("Failed to fdopen");fflush(stdout);
  }
  switch (act)
  { case 'T':  // Terminate simOS
      systemActive = 0; break;
    //case 's':  // submit a program: should not be admin's command
     //program_submission (); break;
    //case 'x':  // execute once, duplicate with y, but more convenient
      //execute_process (); break;
    //case 'y':  // multiple rounds of execution
      //execute_process_iteratively (); break;
    case 'q':  // dump ready queue and list of processes completed IO
      dump_ready_queue (redirect); dump_endIO_list (redirect);fflush(redirect); break;
    case 'r':   // dump the list of available PCBs
      dump_registers (redirect); fflush(redirect);break;
    case 'p':   // dump the list of available PCBs
      dump_PCB_list (redirect); fflush(redirect); break;
    case 'm':   // dump memory of each process
      dump_PCB_memory (redirect); fflush(redirect); break;
    case 'f':   // dump memory frames and free frame list
      dump_memoryframe_info (redirect); fflush(redirect); break;
    case 'n':   // dump the content of the entire memory
      dump_memory (redirect); fflush(redirect); break;
    case 'e':   // dump events in clock.c
      dump_events (redirect); fflush(redirect); break;
    case 't':   // dump terminal IO queue
      dump_termIO_queue (redirect); fflush(redirect); break;
    case 'w':   // dump swap queue
      dump_swapQ (redirect); fflush(redirect); break;
    default:   // can be used to yield to client submission input
      fprintf (infF, "Error: Incorrect command!!!\n");
      printf("INCORRECT COMMMAND");
  }
  fflush(stdout);
}

// Admin has been signaled, got an admin command
// signal handler stores the command somewhere (could be multiple commands)
//       and raise the adcmdInterrupt
// The interrupt handler now calls this function to process the commands
// You need to rewrite this function to retrieve the commands for processing
//       instead of reading them from stdin
// You alson need to change system.c to not to call this function


void process_admin_commands(){
	fflush(stdout); printf("IN PROCESS_ADMIN_COMMANDS %d", childread); fflush(stdout); 

	char action[10];
        read(childread, action, 10);
	printf("Cmd: %s", action);
	char filename[10];	

	if(action[0] == 's'){//submit a program
		read(fnread, &filename, 10);
		char* fnptr= &filename[0];
	//	submit_process (fnptr);	
	}
	else{
		one_admin_command( action[0]); //how to know action?
	}
}


/*
void process_admin_commands ()
{ char action[10];

  while (systemActive)
  { fprintf (infF, "command> ");
    scanf ("%s", action);
    if (uiDebug) fprintf (bugF, "Command issued: %c\n", action[0]);
    // only first action character counts, discard remainder
    // character string is used to capture <ret>
    one_admin_command (action[0]);
  }
}
*/
