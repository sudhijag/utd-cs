#include "simos.h"

#include <unistd.h>
#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <stdlib.h>
#include <netinet/in.h>
#include <string.h>
 

//===============================================================
// The interface to interact with clients for program submission
// --------------------------
// Should change to create server socket to accept client connections
// -- Best is to use the select function to get client inputs
// Should change term.c to direct the terminal output to client terminal
//===============================================================
submitNode *LLHead = NULL;

void dumpQ(){ 
   submitNode *current = NULL;

  printf("Q:\n");
  current = LLHead;
  while (current != NULL){ 
	printf("%s, %d, %d\n", current->name, current->fd, current -> next);
    current = current->next;
  }
}


void pushSubmission (char* filename, char* file, int socketfd){
 // printf("test print: %d, %s, %s", socketfd, filename, file);
  fflush(stdout);
  submitNode *node;

  node = (submitNode *) malloc (sizeof (submitNode));

  //node->  fd= socketfd;
  node -> name = filename;
  node -> contents = file;
  node -> fd= socketfd;
  
  node -> next = LLHead;
  LLHead=node; 
  
  printf("done pushing the node \n");
  dumpQ();
}


int safe(int returnvalue, char* string){
        if(returnvalue < 0){
                printf("%s failed, exiting...", string);
                exit(0);
        }
        else{
                //printf("%s succeeded");
                 return 1;
        }
}

struct sockaddr_in address;
int start_server(){
	//struct sockaddr_in address;	
	int socketfd = socket(AF_INET, SOCK_STREAM, 0);
	safe(socketfd, "socket");

	address.sin_family = AF_INET;
 	address.sin_addr.s_addr = INADDR_ANY;
  	address.sin_port = htons(PORT); 

	int bindval= bind(socketfd, (struct sockaddr *)&address, sizeof(address));
	safe(bindval, "bind");

	int listenval= listen(socketfd, 5);
	safe(listenval, "listen");
	
	return socketfd;
};

void* phase4(void* arguments){
 printf("entering phase 4"); fflush(stdout);
 int socketfd= start_server();
 printf("ready to begin selecting..."); fflush(stdout);

  //starts here
  fd_set all, ready;
  FD_ZERO(&all); FD_ZERO(&ready);
  FD_SET(socketfd, &all);  //use the server socketfd
  
  int j=0;
  int newsocketfd;
  while(1){
    ready = all;

    int ret= select(FD_SETSIZE, &ready, NULL, NULL, NULL);// < 0){
     safe(ret, "select");
	// printf("select failed");
     // return 0;
   // }

    int i=-1;
    while(i < FD_SETSIZE){
	i++;
       if (FD_ISSET(i, &ready)){ 
        if(i != socketfd){
          //read from client
          char buffer[256];
          char buffer2[256];

          bzero(buffer,256);
          int ret = read(i, buffer, sizeof(buffer));
          printf("buffer: %s", buffer);
          if (ret < 0){
            printf("Server ERROR reading filename");
          }
	  
	  if((buffer[0] == 'T' && buffer[1] == '\0') || strcmp(buffer, "T") == 0){
		printf("One of the clients has chosen to terminate."); fflush(stdout);
		FD_CLR(i, &all);
		close(i); //close i?
	  }
	  else{
          	bzero(buffer2,256);
          	int ret2 = read(i, buffer2, sizeof(buffer2));
         	 printf("buffer2: %s", buffer2);
          	if (ret < 0){
            		printf("Server ERROR reading file");
          	}
		
	 	 FILE *fptr = fopen(buffer, "w+"); //holds filename
          	if(fptr == NULL){
            		printf("Can't write to file");
          	}
         	 fwrite(buffer2, sizeof(char), strlen(buffer2), fptr);//write the contents of buffer 2, the file
         	 printf("done writing");
          	fclose(fptr);

          	//pushSubmission (buffer, buffer2, newsocketfd);
          	//fflush(stdout);
  		submitNode *node;

  		node = (submitNode *) malloc (sizeof (submitNode));

    		node -> name = buffer;
      		node -> contents = buffer2;
        	node -> fd= i; //changed from newsocketfd
  
          	node -> next = LLHead;
            	LLHead=node;
  
              	printf("done pushing the node \n");
                dumpQ();
  		fflush(stdout);
		set_interrupt(submitInterrupt);
          }
	}
	else{
          //accept client
          int sasize=sizeof(address);
          newsocketfd = accept(socketfd, (struct sockaddr *)&address, (socklen_t*)&sasize);
          if (newsocketfd < 0){
          	 printf("ERROR accepting");
           }
          else{
          	printf("Accepted!");
                FD_SET(newsocketfd, &all);
          }
        }
      }
    }
  }

  return 0;
} 



pthread_t tid;
void makeSubmitThread(){
  int success = pthread_create (&tid, NULL, &phase4, NULL);
  if (success > 0){
	 printf("Some issue in creating submit thread");
  }
  else{
  	printf("SUBMIT THREAD STARTED"); fflush(stdout);
  }

}

submitNode* sHandler (){ 
  if (LLHead == NULL){ 
    printf ("We cannot remove a node, there are none");
    return NULL; 
  }
  submitNode* temp = LLHead;
  LLHead = LLHead->next;
  dumpQ();
  temp -> next= NULL;//deallocate
  printf("IN SHANDLER"); fflush(stdout); 
   
   submit_process(temp-> fd, temp-> name);
   return temp;
}

