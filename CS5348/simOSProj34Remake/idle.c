// =====================================================
// see https://en.wikipedia.org/wiki/System_Idle_Process
//    for more information about idle process
// ===
// This code segment use a forceful way to quickly load/execute idle process
// Not a good practice, just for convenience. Just ignore the code style.

#include "simos.h"


// also defined and computed in paging ------------
int pageoffsetMask;
int pagenumShift = 0; 
#define OPifgo 5   // has to be consistent with cpu.c
#define opcodeShift 24

void init_loadidle ()
{ pageoffsetMask = pageSize - 1;

  int test = 1; pagenumShift = 0; 
  while (test != pageSize) { test = test << 1; pagenumShift++; }
} //------------------------------------------------


//=========================
// load idle process. Called from loader.c only.
//=========================

// these two direct_put functions are only used for loading idle process
// no specific protection check is done
void direct_put_instruction (int findex, int offset, int instr)
{ int addr = (offset & pageoffsetMask) | (findex << pagenumShift);
  Memory[addr].mInstr = instr;
}

void direct_put_data (int findex, int offset, mdType data)
{ int addr = (offset & pageoffsetMask) | (findex << pagenumShift);
  Memory[addr].mData = data;
}

// load idle process, idle process share OS memory
// We give the last page of OS memory to the idle process
#define OPifgo 5   // has to be consistent with cpu.c
void load_idle_process ()
{ int page, frame;
  int instr, opcode, operand, data;

  init_process_pagetable (idlePid);
  page = 0;   frame = OSpages - 1;
  update_process_pagetable (idlePid, page, frame);
  update_frame_info (frame, idlePid, page);
  
  // load 1 ifgo instructions (2 words) and 1 data for the idle process
  opcode = OPifgo;   operand = 0;
  instr = (opcode << opcodeShift) | operand;
  direct_put_instruction (frame, 0, instr);   // 0,1,2 are offset
  direct_put_instruction (frame, 1, instr);
  direct_put_data (frame, 2, 1);
}


//=========================
// init and execute idle process. Called from process.c only.
//=========================

void init_idle_process ()
{ 
  // create and initialize PCB for the idle process
  PCB[idlePid] = (typePCB *) malloc ( sizeof(typePCB) );

  PCB[idlePid]->Pid = idlePid;  // idlePid = 1, set in ???
  PCB[idlePid]->PC = 0;
  PCB[idlePid]->AC = 0;
  load_idle_process ();
  if (cpuDebug)
    { dump_PCB (bugF, idlePid); dump_process_memory (bugF, idlePid); }
}
// the above function initializes the idle process
// idle process has only 1 instruction, ifgo (2 words) and 1 data
// the ifgo condition is always true and will always go back to 0

void execute_idle_process ()
{ context_in (idlePid);
  CPU.exeStatus = eRun;
  add_timer (idleQuantum, CPU.Pid, actTQinterrupt, oneTimeTimer);
  cpu_execution (); 
}
// idle process will not have page fault, or go to wait state
// or encoutering error or terminate (it is infinite)
// so after execution, no need to check these status
// idle process will run till time quantum is up ==> use idleQuantum (shorter)

