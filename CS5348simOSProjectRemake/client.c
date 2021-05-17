#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <netdb.h>
#include <sys/types.h>

int phase3=0;
//takes in a integer, and sees if it is valid
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

int main(int argc, char *argv[]){
	//Notes: arguments are IP and port_number
	if(argc != 3){
		printf("Improper number of arguments");
	}	
	int portnumber= atoi(argv[2]);

	int fd= socket(AF_INET, SOCK_STREAM, 0);
	safe(socket(AF_INET, SOCK_STREAM, 0), "socket");

	struct sockaddr_in address;
	address.sin_family = AF_INET;
	address.sin_port = htons(portnumber);
	address.sin_addr.s_addr = inet_addr(argv[1]);
	address.sin_addr.s_addr = INADDR_ANY;
	int connection= connect(fd, (struct sockaddr *) &address, sizeof(address));	
	safe(connection, "connect");
	
	char filename[32];
	char filecontents[5000];
	char msg[5000];
	char msg2[32];
	char serverResponse[5000];
	int i=0;
	int writeSuccess;
	
	for(i; i< 1000; i++){
		printf("AT TOP");
		//zero all
		memset(msg2, 0,sizeof(char)*5000);
		bzero(msg, 5000);	
		memset(serverResponse, 0,sizeof(char)*5000);	

		//begin reading
		printf("%s",  "submit filename>");
		scanf("%s", filename);
		
		
		if ( (filename[0] == 'T' && filename[1] == '\0') || strcmp(filename, "T") == 0){
                        printf("Exiting...");
                	//memset(msg2, 0,sizeof(char)*5000);
                	sprintf(msg2, "%s", filename);
                	writeSuccess= write(fd, msg2, strlen(msg2));
                	safe(writeSuccess, "filename send");
			return 0;
		}
		
		//memset(msg2, 0,sizeof(char)*5000);
		sprintf(msg2, "%s", filename);
		writeSuccess= write(fd, msg2, strlen(msg2));
		safe(writeSuccess, "filename send");
	
		FILE *filep = fopen(filename,"r");
                if(filep== NULL){
                        printf("That file doesn't exist in the directory");
                        return;
                }
	
		int numbytes = fread(filecontents, sizeof(char), 5000, filep);
		filecontents[numbytes++] = '\0';
		fclose(filep);

		//memset(msg, 0,sizeof(char) * 5000);
		//bzero(msg, 5000);
		sprintf(msg, "%s", filecontents);
		writeSuccess= write(fd, msg, strlen(msg));
		safe(writeSuccess, "file send");
		
	        char donestr[] = "Process";
		char donestr2[]= "DONE";
		printf("Waiting for response on socket %d\n", fd);
		printf("OUTPUT OF EXECUTION\n");
		do{
			//int readSuccess= read(fd, serverResponse, 5000);
			if(read(fd, serverResponse, 5000) != 0){
				printf("%s \n\n", serverResponse); fflush(stdout);
			}
		//	printf("compare: %s %s %d",serverResponse, donestr, strstr(serverResponse, donestr));
		}while(strstr(serverResponse, donestr) == NULL);
		printf("Done, asking for another file");
	}

	close(fd);
	return 0;
}
