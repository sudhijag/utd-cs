// this header file include global definitions for every system entity
//

//=========================
//general definitions 
//=========================

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <semaphore.h>

//#define Debug 1
int cpuDebug, memDebug, termDebug, swapDebug, clockDebug, uiDebug;
FILE *bugF, *infF;

typedef unsigned *genericPtr;
          // when passing pointers externally, use genericPtr
          // to avoid the necessity of exposing internal structures
          // Now, only clock.c needs this, and used in process.c


//====================================
// sytem.c: configuration parameters and variables for the entire system
//====================================

//cpu and process
int systemActive;
       // indicate whehter system is active,
       // every child thread should test this for termination checking

int maxProcess;    // max number of processes has to < maxProcess
int cpuQuantum;    // time quantum, defined in # instruction-cycles
int idleQuantum;   // time quantum for the idle process

//memory
#define dataSize 4   // each memory unit is of size 4 bytes
#define addrSize 4   // each memory address is of size 4 bytes
int pageSize, numFrames;
       // sizes related to memory and memory management
int loadPpages, maxPpages, OSpages;
       // loadPpages: at load time, #pages allocated to each process
       // maxPpages: max #pages for each process
       // OSpages = #pages for OS, OS occupies the begining of the memory
int agescanPeriod; // the period for scanning and shifting the age vectors
                   // defined in # instruction-cycles
int instrTime;   // instruction execution time (sleep)                   
int termPrintTime;   // simulated time (sleep) for terminal to output a string
int diskRWtime;   // simulated time (sleep) for disk IO (a page)


//============================
// memory.c definitions 
//============================

// memory data type defintion, could be int or float
//typedef int mdType; 
//#define mdInFormat "%d"
//#define mdOutFormat "%d"
typedef float mdType;
#define mdInFormat "%f"
#define mdOutFormat "%.2f"

typedef union     // type definition for memory (its content)
{ mdType mData;
  int mInstr;
} mType;

// memory access return values
#define mNormal 1 
#define mError -1
#define mPFault 0

// define the physical memory, an array of mType,
mType *Memory;

// memory read/write function definitions
int get_data (int offset); 
int put_data (int offset);
int get_instruction (int offset);
  // only cpu.c uses the above 3 functions

void direct_put_instruction (int findex, int offset, int instr);
void direct_put_data (int findex, int offset, mdType data);
  // for loading the idle process, only loader.c uses them
 

//============================
// paging.c definitions (implements memory manager)
//============================

// data structures are local, so, defined in paging.c

  // page table management functions for managing page table of each process
void init_process_pagetable (int pid);
void update_process_pagetable (int pid, int page, int frame);
int free_process_memory (int pid);
void dump_process_pagetable (FILE *outf, int pid);
void dump_process_memory (FILE *outf, int pid);

void  update_frame_info (int findex, int pid, int page);
  // loader.c and memory.c uses the above function

void dump_memory (FILE *outf);
void dump_free_list (FILE *outf);
void dump_memoryframe_info (FILE *outf);

  // interrupt handling functions for memory, called by cpu.c
void page_fault_handler ();
void memory_agescan (); 

void initialize_memory_manager ();   // called by system.c


//============================
// cpu.c related definitions 
//============================

// Pid, Registers and interrupt vector in physical CPU

struct
{ int Pid;  // the pid of the process that is currently running
  int PC;
  mdType AC;
  mdType MBR;
  int IRopcode;
  int IRoperand;
  int *PTptr;   // page table ptr
  int exeStatus;  // execution status
  unsigned interruptV;
  int numCycles;  // this is a global register (clock), not for each process
} CPU;
// some data here are not in CPU registers in realistic systems 


// define interrupt set bit for interruptV in CPU structure
// 1 means bit 0, 4 means bit 2, ...
#define tqInterrupt 1      // for time quantum
#define endIOinterrupt 2  // for any IO completion, including page fault
#define ageInterrupt 4     // for age scan
#define pFaultException 8   // page fault exception
#define adcmdInterrupt 16   // for admin command handling 
#define submitInterrupt 32
        // before setting endIO, caller should add the pid to endIO list

// define exeStatus in CPU structure
#define eRun 1
#define eReady 2
#define ePFault 3
#define eWait 4
#define eEnd 0
#define eError -1


// cpu function definitions

void initialize_cpu ();  // called by system.c
void cpu_execution ();   // called by process.c

void set_interrupt (unsigned bit);  
     // called by clock.c for tqInterrup, memory.c  for ageInterrupt
     // called by clock.c for endIOinterrupt (sleep)
     // called by term.c for endIOinterrupt (termio)
     // called by clock.c for endIOinterrupt (page fault)


//============================
// process.c related definitions 
//============================

typedef struct
{ int Pid;
  int PC;
  mdType AC;
  int *PTptr;
  int exeStatus;
  int timeUsed;
  int numPF;

  int sockfd;
} typePCB;      // data structure for process control block

typePCB **PCB;
  // the system can have at most maxPCB processes,
  // maxProcess further confines it
  // first process is OS, pid=0, second process is idle, pid = 1, 
  // so, pid of any user process starts from 2
  // each process get a PCB, allocate PCB space upon process creation

#define nullPid -1
#define osPid 0
#define idlePid 1


// define process manipulation functions

void dump_PCB (FILE *outf, int pid); 
void dump_ready_queue (FILE *outf);

void insert_endIO_list (int pid); 
  // move all the processes in endIO list to ready queue
  // called by clock.c (sleep), term.c (output), memory.c (page fault)
  // need semaphore protection for the endIO queue access
void endIO_moveto_ready ();
  // called by cpu.c
void dump_endIO_list (FILE *outf);

void initialize_process ();  // called by system.c
int submit_process ( int sockfd, char* fname);  // called by submit.c
  // call loader functions to load the submitted process to swap and memory
  // put the process to ready queue
void execute_process ();  // called by admin.c


//============================
// swap.c related definitions 
//============================

// any read/write to swap space needs to push the request to swapQ
// using this function (by loader.c, memory.c)
void insert_swapQ (int pid, int page, unsigned *buf, int act, int finishact);
  // page is the memor page number;  buf is for read/write data;
  // act can be read or write;  finishact tells what to do after IO is done

// the following are flags for act (action), read or write
#define actRead 0
#define actWrite 1 

// the following are flags for finishact
#define Nothing 1   // flag values for finishact field (what to do after swap)
#define freeBuf 2   // 1: do nothing, 2: swap.c should free the input buffer
#define toReady 4   // 4: swap.c should sesnd the process to ready queue
#define Both    6   // 6: both 2 and 4 (not used)

// other functions
void dump_swapQ (FILE *outf);
int dump_process_swap_page (FILE *outf, int pid, int page);
void start_swap_manager ();  // will create a thread (and initialization)
void end_swap_manager ();  


//============================
// clock.c related definitions 
//============================

genericPtr add_timer (int time, int pid, int action, int recurperiod);
  // system may need to set alarms for various reasons
  // process.c set time quantum, cpu.c sets sleep timer (sleep instruction)
  // memory.c set timer for periodical age scan
  // action: defines the action to be performed after time is up
  // recurperiod: is the periodicity for the peridical timer

#define oneTimeTimer 0  // recurperiod=0: the timer only activates once

  // define actions: the subsequent action to take after timer expires
#define actTQinterrupt 1   // for time quantum
#define actAgeInterrupt 2   // for periodical age scan
#define actReadyInterrupt 3   // 
#define actNull 0

void advance_clock ();  
  // we use #instruction executed as the clock time
  // called by cpu.c to advance clock after executing each instruction
void initialize_timer ();  // called by system.c
void deactivate_timer (genericPtr castedevent);
     // called by process.c when process ends due to error or completed
void dump_events (FILE *outf);  // show the list of timers


//============================
// term.c related definitions 
//============================

void insert_termIO ( int socketfd, int pid, char *outstr, int type);
  // the major function by terminal, for all process print-outs
  // called by cpu.c for print instruction, process.c for end process print
  // need semaphore protection for the endIO queue access

  // type of the terminal request
#define regularIO 1   // indicate that this is a regular IO
#define exitProgIO 0   // indicate that this is the end process IO

// other terminal functions
void dump_termio_queue (FILE *outf);  
void start_terminal ();  // called by system.c
void end_terminal ();  // called by system.c

//======== One function in term.c is put in another file "out.c"
//         for the convenience of changing the code to socket
void terminal_output ( int socketfd,  FILE *fterm, int pid, char *outstr);

//============================
// loader.c related definitions 
//============================

int load_process (int pid, char *fname);
  // This is the major function in loader.c
  // after program submission, load_process loads the program to both
  // memory and swap space (this function is called by process.c)
void load_idle_process ();
  // load an idle process so that CPU will always having something to do

// return status of loader, whether the program being loaded is correct
#define progError -1
#define progNormal 1

//=============== other modules ====================
// admin.c: main program, handles administrator commands
// submit.c: handles program submission
// system.c: reads in system configuration parameters
//           calls initalization and ending functions of other modules 
// idle.c provides loading and execution of the idle process
//============================

void program_submission ();
void process_admin_commands ();

// these are only called in process.c
void init_idle_process (); // init prepares PCB and load the process
void execute_idle_process ();

//=== added by me
void initialize_system();
//char simOSoutput[200];
int timeToRead;
int parentfd;
int childread;
int outputfd;
int fnread;

FILE* redirect;


typedef struct SubmitnodeStruct{
  int fd;
  char* name;
  char* contents;
  
  struct SubmitnodeStruct *next;
} submitNode;

submitNode* sHandler();
int PORT; //this is global and is filled by the cmdline argument?

//sem_t term_semaq, term_mutex;
