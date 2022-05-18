#include <pthread.h>
#include <semaphore.h>
#include "simos.h"

//=========================================================================
// Terminal manager is responsible for printing an output string to terminal,
//    which is simulated by a file "terminal.out"
// Terminal is a separate device from CPU/Mem, so it is a separate thread.
//    Some accesses interfere with the main thread and protection is needed
//    Use semaphores to make it thread safe under concurrent accesses
// When there is an output, the process has to be in eWait state and
//    We insert both pid and the output string to the terminal queue.
// Terminal manager manages the queue and process printing jobs in the queue
// After printing is done, we put the process back to the ready state,
//    which has to be done by the process manager.
//    Terminal only puts the process in endIO queue and set the interrupt.
//=========================================================================

// terminal output file name and file descriptor
// write terminal output to a file, to avoid messy printout
#define termFN "terminal.out"   
FILE *fterm;


void terminal_output (int socketfd, FILE *fterm, int pid, char *outstr)
{
 /* fprintf (fterm, "%s\n", outstr);
  fflush (fterm);*/
  printf("in t_o: %s ", outstr);
 
   char output[256];
  sprintf (output, "%s", outstr);
  printf("Writing to: %d", socketfd); fflush(stdout);
  printf("OUTPUT: %s", output);
  write(socketfd, output, strlen(output));

  usleep (termPrintTime);  // simulate the delay for terminal output
}

//=========================================================================
// terminal queue 
// implemented as a list with head and tail pointers
//=========================================================================

typedef struct TermQnodeStruct
{ int pid, type, socketfd;
  char *str;
  struct TermQnodeStruct *next;
} TermQnode;

TermQnode *termQhead = NULL;
TermQnode *termQtail = NULL;

// if terminal queue is empty, wait on term_semaq
// for each insersion, signal term_semaq
// essentially, term_semaq.count keeps track of #req in the queue
sem_t term_semaq;

// for access the queue head and queue tail
sem_t term_mutex;

// dump terminal queue is not called inside the terminal thread,
// only called by admin.c
void dump_termIO_queue (FILE *outf)
{ TermQnode *node;

  fprintf (outf, "******************** Term Queue Dump\n");
  node = termQhead;
  while (node != NULL)
  { fprintf (outf, "%d, %s\n", node->pid, node->str);
    node = node->next;
  }
  fprintf (outf, "\n");
}

// insert terminal queue is called by the main thread when
//    terminal output is needed (only in cpu.c, process.c)
void insert_termIO (socketfd, pid, outstr, type)
int pid, type, socketfd;
char *outstr;
{ TermQnode *node;

  if (termDebug) fprintf (bugF, "Insert term queue %d %s\n", pid, outstr);
  node = (TermQnode *) malloc (sizeof (TermQnode));
  node->pid = pid;
  node->str = outstr;
  node->type = type;
  node->next = NULL;
  node ->socketfd = socketfd;
  sem_wait (&term_mutex); 
    if (termQtail == NULL) // termQhead would be NULL also
      { termQtail = node; termQhead = node; }
    else // insert to tail
      { termQtail->next = node; termQtail = node; }
  sem_post (&term_mutex); 
  sem_post (&term_semaq); 
  if (termDebug) dump_termIO_queue (bugF);
}

// remove the termIO job from queue and call terminal_output for printing
// after printing, put the job to endIO list and set endIO interrupt
void handle_one_termIO ()
{ TermQnode *node;

  sem_wait (&term_semaq);
  if (termDebug) dump_termIO_queue (bugF);
  if (termQhead == NULL)
  { if (systemActive)
      fprintf (infF, "Error: No process in term queue after sem_wait!!!\n");
  }
  else 
  { node = termQhead;
    terminal_output ( node ->  socketfd, fterm, node->pid, node->str);

    if (node->type != exitProgIO) // *** ADD CODE:
    { insert_endIO_list (node->pid);
      set_interrupt (endIOinterrupt);
    } // if it is of exitProgIO type, then job done, just clean termIO queue

    if (termDebug)
      fprintf (bugF, "Remove term queue %d %s\n", node->pid, node->str);
    sem_wait (&term_mutex); 
      termQhead = node->next;
      if (termQhead == NULL) termQtail = NULL;
      free (node->str); free (node);
    sem_post (&term_mutex); 
    if (termDebug) dump_termIO_queue (bugF);
  }
}


//=====================================================
// loop on handle_one_termIO to process the termIO requests
// This has to be a separate thread to loop for request handling
//=====================================================

void *termIO ()
{
  while (systemActive) handle_one_termIO ();
  if (termDebug) fprintf (bugF, "TermIO loop has ended\n");
}

// initializing the terminal device
// terminal thread is created here and this function is invoked by system.c
pthread_t termThread;

void start_terminal ()
{ int ret;

  fterm = fopen (termFN, "w");
  // *** ADD CODE:
  sem_init (&term_semaq, 0, 0);
  sem_init (&term_mutex, 0, 1);
  ret = pthread_create (&termThread, NULL, termIO, NULL);
  if (ret < 0) fprintf (infF, "TermIO thread creation problem\n");
  else fprintf (infF, "TermIO thread has been created successsfully\n");
}

// cleaning up and exit the terminal thread, invoked by system.c
void end_terminal ()
{ int ret;

  fclose (fterm);
  // *** ADD CODE:
  sem_post (&term_semaq);  // signal in case handler is waiting on Null queue
      // no problem if we post additional signals becuase
      // the null queue is always checked anyway
  ret = pthread_join (termThread, NULL);
    // this function is not called locally, but by main thread (system.c)
  fprintf (infF, "TermIO thread has terminated %d\n", ret);
}


