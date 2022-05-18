#include "simos.h"


//==========================================
// run time memory access operations, called by cpu.c
//==========================================

extern int calculate_memory_address (unsigned offset, int rwflag);
// This is the function used by every operation here, but since it
// depends on the memory management mechanism, it is implemented outside

// rwflag: whehter the addr computation should be for read or for write
// also defined in paging.c
#define flagRead 1
#define flagWrite 2

// define shifts and masks for instruction and memory address 
#define opcodeShift 24
#define operandMask 0x00ffffff

int get_data (int offset)
{ int maddr;

  maddr = calculate_memory_address (offset, flagRead);
  if (maddr == mError || maddr == mPFault) return (maddr);
  else
  { CPU.MBR = Memory[maddr].mData;
    return (mNormal);
  }
}

int put_data (int offset)
{ int maddr; 

  maddr = calculate_memory_address (offset, flagWrite);
  if (maddr == mError || maddr == mPFault) return (maddr);
  else
  { Memory[maddr].mData = CPU.MBR; //we write to memory the value in the MBR
    return (mNormal);
  }
}

int get_instruction (int offset)
{ int maddr, instr; 

  maddr = calculate_memory_address (offset, flagRead);
  if (maddr == mError || maddr == mPFault) return (maddr);
  else
  { instr = Memory[maddr].mInstr;
    CPU.IRopcode = instr >> opcodeShift; 
    CPU.IRoperand = instr & operandMask;
    return (mNormal);
  }
}

void initialize_physical_memory ()
{ int i;

  int memSize = memSize = numFrames * pageSize;
      // config.sys input, read in system.c: pageSize, numFrames
  // create memory of memSize
  Memory = (mType *) malloc (memSize*sizeof(mType));
  for (i=0; i<memSize; i++) Memory[i].mInstr = 0;
}

