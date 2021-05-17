#include <iostream>
#include <fstream>
#include <cstdlib>
#include <ctime>
#include <cctype>

using namespace std;

ifstream in_stream;
ofstream out_stream;

void encrypt(ifstream& in_stream, ofstream& out_stream);//Takes in plaintext, spits out ciphertext
void decrypt(ifstream& in_stream, ofstream& out_stream);//Takes in ciphertext, spits out plaintext

int main(){
    char direction;//Decides whether to encrypt or decrypt the file
    char in_file_name[32], out_file_name[32];//The user inputs these
    bool valid=false;
    
    while(!valid){
        cout << "Would you like to encrypt or decrypt a file (E/D)?";
        cin >> direction;
        
        if(direction == 'E' || direction == 'e' || direction == 'D' || direction == 'd'){
            valid=true;
        }
    }//Continues to prompt until valid input is entered
    
    if(direction == 'E' || direction == 'e'){
        cout << "Enter the file to be encrypted: ";
        cin >> in_file_name;
        cout << "Enter the output file: ";
        cin >> out_file_name;
        
        in_stream.open(in_file_name);
        if(in_stream.fail()){
            cout << "Input file failed to open" << endl;
            exit(EXIT_FAILURE);
        }
        
        out_stream.open(out_file_name);
        if(out_stream.fail()){
            cout << "Output file failed to create" << endl;
            exit(EXIT_FAILURE);
        }
        cout << "I'm going to encrypt " << in_file_name << " and put it in " << out_file_name << endl;
        encrypt(in_stream, out_stream);
    }
    else{
        cout << "Enter the file to be decrypted: ";
        cin >> in_file_name;
        cout << "Enter the output file: ";
        cin >> out_file_name;
        
        in_stream.open(in_file_name);
        if(in_stream.fail()){
            cout << "Input file failed to open" << endl;
            exit(EXIT_FAILURE);
        }
        
        out_stream.open(out_file_name);
        if(out_stream.fail()){
            cout << "Output file failed to create" << endl;
            exit(EXIT_FAILURE);
        }
        cout << "I'm going to decrypt " << in_file_name << " and put it in " << out_file_name << endl;
        decrypt(in_stream, out_stream);
    }
    
    in_stream.close();
    out_stream.close();
    
    cout << endl << endl;
    return 0;
}

/*
	Name: Encrypt
	Parameters: An input and output stream passed by reference
	Return: void
	Description: Takes in a plaintext file and generates random keys to encrypt it
 */


void encrypt(ifstream& istream, ofstream& ostream){
    ofstream ostream2;
    char key_file_name[32];
    cout << "Enter name of file containing encryption keys: ";
    cin >> key_file_name;
    ostream2.open(key_file_name);
    
    if(ostream2.fail()){
        cout << "Key file failed to open!";
        exit(EXIT_FAILURE);
    }
    
    srand(time(NULL));//seeds random number
    
    char c;
    int shift;
    while(!istream.eof()){
        istream >> c;
        shift = rand() % 275 +3;
        if(isalpha(c)){
            shift = shift % 26;
            ostream2 << shift;
            ostream2.put(' ');
            //cout << "old c: " << c << endl;
            //cout << "Shift is: " << shift << endl;
            
            if(isupper(c)){
                //cout << "TESTING..." << endl;
                c= c - 'A';
                c= c + shift;
                c %= 26;
                c = c + 'A';
                //cout << "new c: " << c << endl;
                ostream.put(c);
            }
            else if(islower(c)){//Handles special overflow cases where the limit of char is exceeded
                if(!istream.eof()){
                    c = c - 'a';
                    c = c + shift;
                    c %= 26;
                    c = c + 'a';
                    //cout << "new c" << c << endl;
                    ostream.put(c);
                }
            }
            else{
                if(istream.eof()){
                    cout << "WARNING: Cannot determine character type" << endl;
                    ostream.put(c);
                }
            }
        }//end isalpha
        else if(ispunct(c)){
            if(!istream.eof()){
                //cout << "No shift, this is punctuation" << endl;
                ostream.put(c);
            }
        }
        else if(isdigit(c)){
            //cout << "It's a digit: " << c << endl;
            shift = shift % 10;
            if(!istream.eof()){
                c = c + shift;
                c = c % 10;//Gets the units digit
                //cout << "NEW C: " << c << endl;
                ostream.put(c);
            }
        }
        else{
            if(!istream.eof()){
                cout << "WARNING: Cannot determine character type";
                ostream.put(c);
            }
        }
    }
    cout << "Finished writing to file";
}


/*
 Name: Decrypt
 Parameters: An input and output stream passed by reference
 Return: void
 Description: Takes in a ciphertext file and random keys and decrypts it
 */

void decrypt(ifstream& istream, ofstream& ostream){
    ifstream istream2;
    char key_file_name[32];
    cout << "Enter name of file containing encryption keys: ";
    cin >> key_file_name;
    istream2.open(key_file_name);
    
    if(istream2.fail()){
        cout << "Key file failed to open!";
        exit(EXIT_FAILURE);
    }
    
    char c;
    int shift;
    while(!istream.eof()){
        istream >> c;
        istream2 >> shift;
        if(!istream.eof()){
            if(isalpha(c)){
                //cout << "old c: " << c << endl;
                //cout << "Shift is: " << shift << endl;
                
                shift %= 26;
                if(isupper(c)){//Handles uppercase letters
                    if(c - shift < 'A'){//to prevent c from going behind 'A'
                        shift = 26-shift;
                        c += shift;
                    }
                    else{
                        c -= shift;
                    }
                    ostream.put(c);
                    //cout << "new c is" << c << endl << endl;
                }
                else if(islower(c)){//Handles lowercase letters
                    if(c - shift < 'a'){//To prevent c from going behind 'a'
                        shift= 26-shift;//Finds the complement
                        c += shift;
                    }
                    else{
                        c -= shift;
                    }
                    //cout << "new c is" << c << endl << endl;
                    ostream.put(c);
                }
                else{
                    cout << "ERROR!" << endl;       
                } 
            }//end isalpha
            else if(isdigit(c)){//Handles 0-9 digits
                shift %= 10;
                c = c + 10;
                c = c - shift;
                c = c % 10;
                ostream.put(c);			
            }
            else if(ispunct(c)){//Handles punctuation
                //cout << "No shift, this is punctuation" << endl;
                ostream.put(c);
            }
            else{
                cout << "WARNING: Cannot determine character type";
                ostream.put(c);
            }
        }
    }
    cout << "Finished writing to file" << endl;
}
