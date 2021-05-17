#include "simos.h"

#define maxCPUcycles 1024*1024*1024 // = 2^30

void check_timer ();

void advance_clock ()
{ CPU.numCycles++;
  // in a real system, timer is checked on clock cycles
  // here we use CPU cycle for timer, thus, advance time is done in cpu.c
  // after each instruction execution

  if (CPU.numCycles > maxCPUcycles)
    { fprintf (infF, "CPU cycle count exceeds its limit!!!\n"); exit(-1); }
  // we use maxCPUcycle here to prevent integer overflow
  check_timer ();
}

// We need to build a timer list, in sorted order
// only the first event in the list will be checked to see 
// whether its time is up
// To make insertion efficient, we keep a binary tree of events
// while using a eventHead pointer to point to the leftmost node 
//
// eventNode is defined to keep track of timer events and 
// to maintain the event tree (and list)
// The fields: time, pid, act, recurP belong to the timer event level
// The fields: left, right, parent belong to the tree data structure level

struct eventNode
{ int time;   // in number of instruction cycles, relative to absolute time
  int pid;    // if action = actReady, put pid in  ready queue
              // for other actions pid is ignored (can be set to 0)
  int act;    // action to be performed when timer expires
  int recurP; // if it is not a recurring timer, then this is 0; 
              // else this is the recurring period
  struct eventNode *left, *right;
  struct eventNode *parent;
              // keep parent node to make removal of head node easier
} *eventTree, *eventHead;


// the event tree has a dummy node to begin with, with the highest time
void initialize_eventtree ()
{ 
  eventTree = (struct eventNode *) malloc (sizeof (struct eventNode));
  eventTree->time = maxCPUcycles + 1;
  eventTree->pid = 0;
  eventTree->act = 0;
  eventTree->recurP = 0;
  eventTree->left = NULL;
  eventTree->right = NULL;
  eventTree->parent = NULL; 
  eventHead = eventTree;
}

void insert_event (event)
struct eventNode *event;
{ struct eventNode *cnode;

  event->left = NULL;
  event->right = NULL;
  
  cnode = eventTree;
  while (cnode != NULL)
  { if (event->time < cnode->time) 
      if (cnode->left == NULL)
      { cnode->left = event;
        event->parent = cnode;
        if (eventHead == cnode) eventHead = event;
         // the new event has a lower time, eventHead should point to it
        break;
      }
      else cnode = cnode->left;
    else // event->time >= tree->time
    { if (cnode->right == NULL)
      { cnode->right = event;
        event->parent = cnode;
        break;
      }
      else cnode = cnode->right;
    }
  }
}

// only remove the eventHead, which is always leftmost node in the tree
// first update the eventHead, then update the tree
// note: freeing the node is not done here, recurring event reuses node 

void remove_eventhead ()
{ struct eventNode *event, *temp;

  event = eventHead;
  if (event->right != NULL)
  { temp = event->right;
    while (temp->left != NULL) temp = temp->left;
    eventHead = temp;
    event->right->parent = event->parent;
  }
  else eventHead = event->parent;
  event->parent->left = event->right;
}

// recursive call to list all events in the event tree
// external  caller should call dump_events()
void list_events (FILE *outf, struct eventNode *event)
{
  if (event != NULL)
  { fprintf (outf, "Event: time=%d, pid=%d, action=%d, recurP=%d, ",
             event->time, event->pid, event->act, event->recurP);
    if (event->left != NULL) fprintf (outf, "left=%d, ", event->left->time);
    else fprintf (outf, "left=null, ");
    if (event->right != NULL) fprintf (outf, "right=%d\n", event->right->time);
    else fprintf (outf, "right=null\n");
    list_events (outf, event->left);
    list_events (outf, event->right);
  }
}

void dump_events (FILE *outf)
{ fprintf (outf, "Now = %d, Head: time=%d, pid=%d, action=%d, recurP=%d\n",
           CPU.numCycles, eventHead->time,
           eventHead->pid, eventHead->act, eventHead->recurP);
  list_events (outf, eventTree);
}


//=============================================================
// high level timer calls
//

void initialize_timer ()
{ 
  initialize_eventtree();
}

genericPtr add_timer (time, pid, action, recurperiod)
int time, pid, action, recurperiod; // time is from current time
{ struct eventNode *event;

  time = CPU.numCycles + time;
    // caller gives the relative time, so need to change to absolute time
  if (time > maxCPUcycles)
  { fprintf (infF, "timer exceeds CPU cycle limit!!!\n"); exit(-1); }
  else 
  { event = malloc (sizeof (struct eventNode));
    event->time = time;
    event->pid = pid;
    event->act = action;
    event->recurP = recurperiod;
    insert_event (event);
    if (clockDebug)
      fprintf (bugF, "Add timer: time=%d, pid=%d, action=%d, recurP=%d\n",
               event->time, event->pid, event->act, event->recurP);
    return ((genericPtr) event);
      // to not expose the eventNode structure, a casted pointer is returned
  }
}

void check_timer ()
{ struct eventNode *event;

  while (eventHead->time <= CPU.numCycles)
  { event = eventHead;
    if (clockDebug)
    { fprintf (bugF, "Process event: time=%d, pid=%d, action=%d, recurP=%d\n",
              event->time, event->pid, event->act, event->recurP);
      fprintf (bugF, "Check timer: interrupt = %x ==> ", CPU.interruptV);
    }
    switch (event->act)
    { case actTQinterrupt:
        set_interrupt (tqInterrupt);
        break;
      case actAgeInterrupt:
        set_interrupt (ageInterrupt);
        break;
      case actReadyInterrupt:
        insert_endIO_list (event->pid);
        set_interrupt (endIOinterrupt);
        break;
      case actNull:
        if (clockDebug)
          fprintf (bugF, "Event: time=%d, pid=%d, action=%d, recurP=%d\n",
                  event->time, event->pid, event->act, event->recurP);
        break;
      default:
        fprintf (infF, "Encountering an illegitimate action code\n");
        break;
    }
    remove_eventhead ();
    if (event->recurP > 0) // recurring event, put the event back
    { event->time = CPU.numCycles + event->recurP;
      if (event->time > maxCPUcycles)
      { fprintf (infF, "timer exceeds CPU cycle limit!!!\n"); exit(-1); }
      else insert_event (event);
    }
    else free (event);
    if (clockDebug)
    { fprintf (bugF, " %x\n", CPU.interruptV); dump_events (bugF); }
  }
}

// deactivate event simply sets the after-event-action to NULL
//   (deactivate when the process stops execution (IO, sleep, etc.)
// We could remove it, but no point
//   Removing an intermediate node is more expensive and requires
//   the data structure to maintain more information
//   So, simply wait till it becomes the eventHead and get removed
//   Of course, the insertion time may increase
// We uses eventNode ptr to get to the target event node
// The event node may be removed for action before it is freed, a problem!!!
//   won't happen in simOS, our clock will not advance without instr exec
//   otherwise, more complex handling can take care of the problem
void deactivate_timer (castedevent)
genericPtr castedevent;
{ struct eventNode *event;

  event = (struct eventNode *) castedevent;
  if (event != NULL) event->act = actNull;
  else fprintf (infF, "Deactivate a timer, but the timer has expired\n");
  if (clockDebug) 
  { fprintf(bugF,
           "Deactivate event: addr=%x, time=%d, pid=%d, action=%d, reP=%d\n",
           castedevent, event->time, event->pid, event->act, event->recurP);
    dump_events (bugF);
  }
}


