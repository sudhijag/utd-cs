#include "simos.h"
#include<stdio.h> 
#include<stdlib.h> 
#include<unistd.h> 
#include<sys/types.h> 
#include<string.h> 
#include<sys/wait.h> 

void handler(){
	fflush(stdout);printf("IN HANDLER, interrruptV is: %x", CPU.interruptV); fflush(stdout);
	set_interrupt(adcmdInterrupt);		
	fflush(stdout);printf("IN HANDLER, NEW interruptV is: %x", CPU.interruptV); fflush(stdout);
}

int main(int argc, char *argv[] ) { 

    //The two pipes wil support communication between the admin process and the simOS process
    int fd1[2]; //parent write, child read 
    int fd2[2]; //parent read,  child write

    int fn[2]; //this is only for the parent to write the filename
   int numR= atoi(argv[1]); //this is the number of roudns to run execute_process
    PORT= atoi(argv[2]); //for now hardcoded		

    pid_t p; 
  
    if (pipe(fd1)==-1 || pipe(fd2)==-1 || pipe(fn) == -1) { 
        printf("Some pipe creation failed" ); 
        return 1; 
    } 
    
	signal(SIGINT, handler);
	//signal(SIGRTMAX, handler2);
	p = fork(); 
  
    if (p < 0) { 
        printf("Fork creation failed" ); 
        return 1; 
    } 
 
    // child
    else if (p==0){
       close(fn[1]);
       close(fd1[1]);//no need to write to this pipe
       close(fd2[0]);//or read from this one

        int i=0;
 	systemActive = 1;//controls thread loops
        initialize_system (); 
	makeSubmitThread();	
	
	printf("We want to write to %d", fd2[1]);
	outputfd = fd2[0];
	parentfd= fd2[1];
        childread= fd1[0]; 
	fnread= fn[0];
			
	printf("Enter loop");
	int roundsSoFar;
	while(systemActive){
		execute_process();
	} 
	
	printf("System is no longer active!"); fflush(stdout);	
	//systemActive=0;

	printf ("numR done, system exit!\n");
	fflush(stdout);
  	system_exit ();
        exit(0);
    }
 
 //parent	
     else{
		close(fn[0]);
		close(fd1[0]);//no need to read from this pipe
        	close(fd2[1]);//or write to this one
		printf("Parent");
		char action[10];
		char filename[10];
		int i=0;

  		while (1){
	//	for(i;i<numR;i++){
	
			fflush(stdout); printf("AT TOP"); fflush(stdout);
    		//	do{
				printf ( "command> ");
				scanf ("%s", action);
				printf("Echo: %c", action[0]);
			/* if(action[0]=='s'){	
				printf("Submission file: ");
				scanf("%s", filename);
				if( write(fn[1], &filename, strlen(filename) +1) <0){
					printf("That write failed");
					return 0;
				}
			}*/
				//printf("DON'T SUBMIT PROGRAMS THROUGH ADMIN");
		//	}while(action[0] == 's');
			if(action[0] == 's'){
				 printf("DON'T SUBMIT PROGRAMS THROUGH ADMIN");
				return 0;
			}
			if( write(fd1[1], &action[0], sizeof(char)) < 0){
				printf("this write failed");
				return 0;
			}
			kill(0, SIGINT);
			printf("Now waiting to read", timeToRead);fflush(stdout);
			char myOutput[200];
                        read(fd2[0], &myOutput, 200);   
                        printf("OUTPUT FROM CHILD: %s", myOutput);
                }
  		
		printf("parent process exiting b/c system not active...");
		wait(NULL);
 
      }
	
	return 0;
} 
