# Names:
#	Akilan Gnanavel
#	Sudarshana Jagadeeshi
# NET IDs:
# 	AXG180113
#	SXJ180060

# COMPLETED BONUS TO DOCUMENT
#	Victory music when user wins
#	Ding sound when user places a coin

.data
	intro: .asciiz "Welcome to Connect 4 made by Akilan Gnanavel and Sudarshana Jagadeeshi\n"
	current: .asciiz "It is currently your turn\n"
	invalid: .asciiz "That column is full. Please pick a different column: "
	invalid2: .asciiz "That is not a valid column number. Please pick a different column."
	prompt: .asciiz "Please enter the column of where you would like to insert your piece: "
	clarify: .asciiz "0 represents an empty slot, 1 represents the user's (your) coin, and 2 represents the computer's coin"
	newline: .asciiz "\n"
	numpieces: .word 0
	numblue: .word 0
	numred: .word 0
	tiemessage: .asciiz "You tied with the computer!\n"
	winmessage: .asciiz "You have beaten the computer!\n"
	losemessage: .asciiz "You have lost to the computer!\n"
	outro: .asciiz "Thanks for playing Connect 4 made by Akilan Gnanavel and Sudarshana Jagadeeshi"
	char1: .word '|'
	row1: .word 0, 0, 0, 0, 0, 0
	row2: .word 0, 0, 0, 0, 0, 0
	row3: .word 0, 0, 0, 0, 0, 0
	row4: .word 0, 0, 0, 0, 0, 0
	row5: .word 0, 0, 0, 0, 0, 0
	row6: .word 0, 0, 0, 0, 0, 0
	row7: .word 0, 0, 0, 0, 0, 0
	.eqv DATA_SIZE 4
	winner: .word 0 # 1 = user, 2 = computer

.text
start:	
	# print the introduction
	li $v0, 4
	la $a0, intro
	syscall
	
	# begin the game loop
	j userturn

# this code simulates the user's turn in the game	
userturn:
	# end the game with a tie if the board is full
	lw $t1, numpieces
	beq $t1, 42, tie
	
	j checklose

userturncontinue:	
	# call the subroutine to print the board
	jal printboard
	
	# prompt the user for the column number
	li $v0, 4
	la $a0, current
	syscall
	la $a0, prompt
	syscall
	
	
	# get the column number from the user
	li $v0, 5
	syscall
	
	li $t9, 1
	li $t8, 6
	blt $v0, $t9, userturncontinue2
	bgt $v0, $t8, userturncontinue2
	
	# call the subroutine to insert the coin with arguments (adjusting for index offset)
	move $a0, $v0
	addi $a0, $a0, -1
	j updateboard

# if the user entered an invalid column number
userturncontinue2:
	li $v0, 4
	la $a0, invalid2
	syscall
	
	la $a0, newline
	syscall
	
	# re-prompt the user
	j userturncontinue	
			
return:
	# increment the number of pieces on the board
	# because at the end of the loop, at least one piece will have been placed
	lw $t0, numpieces
	addi $t0, $t0, 1
	sw $t0, numpieces
	
	# it is now the computer's turn
	j computerturn
	
# this code simulates the computer's turn in the game	
computerturn:
	# end the game with a tie if the board is full
	lw $t1, numpieces
	beq $t1, 42, tie
	
	j checkwin
	
computerturncontinue:	
	# generate random number for computer move
	li $a1, 6
	li $v0, 42
	syscall
	
	j checkr7

# check every row to make sure there is room
# if there is room, add it in
checkr7:
	la $t0, row7
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr6
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr6:
	la $t0, row6
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr5
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr5:
	la $t0, row5
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr4
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr4:
	la $t0, row4
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr3
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr3: 
	la $t0, row3
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr2
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr2: 
	la $t0, row2
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkr1
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
checkr1:
	la $t0, row1
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	# TODO
	# if we reached this point, then the column is full
	# 	figure out what to do at this point to prompt user again
	bne $t3, 0, return2 # <---- replace this with code to handle when the entire column is filled
	
	li $t4, 2
	sw $t4, 0($t2)
	
	j return2
	
return2:
	# increment the number of pieces on the board
	# because at the end of the loop, at least one piece will have been placed
	lw $t0, numpieces
	addi $t0, $t0, 1
	sw $t0, numpieces
	
	# it is now the user's turn
	j userturn

# this code terminates the program after letting the user know that he/she tied
tie:
	# print the result
	jal printboard
	li $v0, 4
	la $a0, tiemessage
	syscall
	
	# print the outro thanks message
	li $v0, 4
	la $a0, outro
	syscall
	
	# terminate the program
	li $v0, 10
	syscall

# this code terminates the program after letting the user know that he/she won
won:
	# print the result
	jal printboard
	li $v0, 4
	la $a0, winmessage
	syscall
	
	# print the outro thanks message
	li $v0, 4
	la $a0, outro
	syscall
	
	jal victorymusic
	
	# terminate the program
	li $v0, 10
	syscall

# this code terminates the program after letting the user know that he/she lost
lost:
	# print the result
	jal printboard
	li $v0, 4
	la $a0, losemessage
	syscall
	
	# print the outro thanks message
	li $v0, 4
	la $a0, outro
	syscall
	
	# terminate the program
	li $v0, 10
	syscall
	
# this subroutine changes the board using the requested column as an argument
updateboard:
	# TODO
	# NOTE: $a0 CONTAINS THE COLUMN THAT THE USER REQUESTED
	# MAKE SURE THE COLUMN NUMBER IS VALID (HAS ROOM)
	
	# start checking from bottom of column
	j checkrow7

# check every row to make sure there is room	
# if there is, add the coin
checkrow7:
	la $t0, row7
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow6
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow6:
	la $t0, row6
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow5
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow5:
	la $t0, row5
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow4
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow4:
	la $t0, row4
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow3
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow3: 
	la $t0, row3
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow2
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow2: 
	la $t0, row2
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	bne $t3, 0 checkrow1
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return
	
checkrow1:
	la $t0, row1
	move $t1, $a0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	# TODO
	# if we reached this point, then the column is full
	# 	figure out what to do at this point to prompt user again
	bne $t3, 0, redo # <---- replace this with code to handle when the entire column is filled
	
	li $t4, 1
	sw $t4, 0($t2)
	
	# play ding sound
	li $v0,31
	li $a0, 80
	li $a1, 200
	li $a3, 127
	syscall
	
	j return

# if the column was invalid or full, ask the user to redo it	
redo:
	la $a0, invalid
	li $v0, 4
	syscall
	
	li $v0, 1
	syscall
	
	j checkrow7

# this subroutine prints out the current state of the board in formatted ASCII
printboard:
	la $a0, clarify
	li $v0, 4
	syscall
	
	la $a0, newline
	syscall
	
	# print row 1
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 2
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 3
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 4
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 5
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 6
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# print row 7
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	li $v0, 1
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $a0, 0($t2)
	syscall
	
	li $v0, 4
	la $a0, char1
	syscall
	
	la $a0, newline
	syscall
	
	# return to the game loop
	jr $ra

# IMPORTANT
#	THE FOLLOWING CODE (UNTIL THE END OF THE PROGRAM) CHECKS FOR THE WIN CONDITIONS OF THE GAME AFTER EVERY TURN
#	THE FIRST HALF CHECKS ALL POSSIBILITIES OF THE USER WINNING AND
# 	THE SECOND HALF CHECKS ALL POSSIBILITIES OF THE COMPUTER WINNING		
checkwin:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a
	j checkwin2

a:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, bz
	j checkwin2

bz: 
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c
	j checkwin2

c:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin2

checkwin2:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d
	j checkwin3
	
d:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e
	j checkwin3
	
e:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f
	j checkwin3
	
f:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin3

checkwin3:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g
	j checkwin4
	
g:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h
	j checkwin4

h:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i
	j checkwin4

i:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin4
	
checkwin4:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a1
	j checkwin5

a1:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b1
	j checkwin5

b1: 
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c1
	j checkwin5

c1:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin5

checkwin5:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d1
	j checkwin6
	
d1:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e1
	j checkwin6
	
e1:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f1
	j checkwin6
	
f1:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin6

checkwin6:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g1
	j checkwin7
	
g1:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h1
	j checkwin7

h1:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i1
	j checkwin7

i1:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin7
	
checkwin7:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a2
	j checkwin8

a2:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b2
	j checkwin8

b2: 
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c2
	j checkwin8

c2:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin8

checkwin8:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d2
	j checkwin9
	
d2:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e2
	j checkwin9
	
e2:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f2
	j checkwin9
	
f2:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin9

checkwin9:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g2
	j checkwin10
	
g2:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h2
	j checkwin10

h2:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i2
	j checkwin10

i2:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin10
	
checkwin10:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a3
	j checkwin11

a3:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b3
	j checkwin11

b3: 
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c3
	j checkwin11

c3:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin11

checkwin11:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d3
	j checkwin12
	
d3:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e3
	j checkwin12
	
e3:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f3
	j checkwin12
	
f3:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin12

checkwin12:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g3
	j checkwin13
	
g3:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h3
	j checkwin13

h3:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i3
	j checkwin13

i3:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin13
	
checkwin13:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a4
	j checkwin14

a4:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b4
	j checkwin14

b4: 
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c4
	j checkwin14

c4:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin14

checkwin14:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d4
	j checkwin15
	
d4:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e4
	j checkwin15
	
e4:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f4
	j checkwin15
	
f4:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin15

checkwin15:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g4
	j checkwin16
	
g4:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h4
	j checkwin16

h4:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i4
	j checkwin16

i4:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin16
	
checkwin16:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a5
	j checkwin17

a5:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b5
	j checkwin17

b5: 
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c5
	j checkwin17

c5:
	la $t0, row2
	
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin17

checkwin17:
	la $t0, row2
	
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d5
	j checkwin18
	
d5:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e5
	j checkwin18
	
e5:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f5
	j checkwin18
	
f5:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin18

checkwin18:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g5
	j checkwin19
	
g5:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h5
	j checkwin19

h5:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i5
	j checkwin19

i5:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin19
	
checkwin19:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, a6
	j checkwin20

a6:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, b6
	j checkwin20

b6: 
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, c6
	j checkwin20

c6:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin20

checkwin20:
	la $t0, row1	
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, d6
	j checkwin21
	
d6:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, e6
	j checkwin21
	
e6:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, f6
	j checkwin21
	
f6:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin21

checkwin21:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, g6
	j checkwin22
	
g6:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, h6
	j checkwin22

h6:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, i6
	j checkwin22

i6:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, won
	j checkwin22

checkwin22:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j1
	j checkwin23

j1:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k1
	j checkwin23

k1:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l1
	j checkwin23
	
l1:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin23
	
checkwin23:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j2
	j checkwin24

j2:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k2
	j checkwin24

k2:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l2
	j checkwin24
	
l2:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin24
	
checkwin24:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j3
	j checkwin25

j3:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k3
	j checkwin25

k3:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l3
	j checkwin25
	
l3:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin25

checkwin25:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j4
	j checkwin26

j4:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k4
	j checkwin26

k4:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l4
	j checkwin26
	
l4:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin26
	
checkwin26:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j5
	j checkwin27

j5:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k5
	j checkwin27

k5:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l5
	j checkwin27
	
l5:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin27
	
checkwin27:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j6
	j checkwin28

j6:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k6
	j checkwin28

k6:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l6
	j checkwin28
	
l6:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin28

checkwin28:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j7
	j checkwin29

j7:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k7
	j checkwin29

k7:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l7
	j checkwin29
	
l7:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin29
	
checkwin29:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j8
	j checkwin30

j8:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k8
	j checkwin30

k8:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l8
	j checkwin30
	
l8:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin30
	
checkwin30:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j9
	j checkwin31

j9:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k9
	j checkwin31

k9:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l9
	j checkwin31
	
l9:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin31

checkwin31:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j10
	j checkwin32

j10:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k10
	j checkwin32

k10:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l10
	j checkwin32
	
l10:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin32
	
checkwin32:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j11
	j checkwin33

j11:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k11
	j checkwin33

k11:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l11
	j checkwin33
	
l11:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin33
	
checkwin33:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j12
	j checkwin34

j12:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k12
	j checkwin34

k12:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l12
	j checkwin34
	
l12:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin34
	
checkwin34:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j13
	j checkwin35

j13:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k13
	j checkwin35

k13:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l13
	j checkwin35
	
l13:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin35
	
checkwin35:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j14
	j checkwin36

j14:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k14
	j checkwin36

k14:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l14
	j checkwin36
	
l14:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin36
	
checkwin36:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j15
	j checkwin37

j15:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k15
	j checkwin37

k15:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l15
	j checkwin37
	
l15:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin37

checkwin37:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j16
	j checkwin38

j16:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k16
	j checkwin38

k16:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l16
	j checkwin38
	
l16:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin38
	
checkwin38:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j17
	j checkwin39

j17:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k17
	j checkwin39

k17:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l17
	j checkwin39
	
l17:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin39
	
checkwin39:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j18
	j checkwin40

j18:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k18
	j checkwin40

k18:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l18
	j checkwin40
	
l18:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin40

checkwin40:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j19
	j checkwin41

j19:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k19
	j checkwin41

k19:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l19
	j checkwin41
	
l19:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin41
	
checkwin41:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j20
	j checkwin42

j20:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k20
	j checkwin42

k20:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l20
	j checkwin42
	
l20:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin42
	
checkwin42:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j21
	j checkwin43

j21:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k21
	j checkwin43

k21:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l21
	j checkwin43
	
l21:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin43

checkwin43:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j22
	j checkwin44

j22:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k22
	j checkwin44

k22:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l22
	j checkwin44
	
l22:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin44
	
checkwin44:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j23
	j checkwin45

j23:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k23
	j checkwin45

k23:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l23
	j checkwin45
	
l23:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin45
	
checkwin45:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, j24
	j checkwin46

j24:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, k24
	j checkwin46

k24:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 1, l24
	j checkwin46
	
l24:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin46 

checkwin46:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m1
	j checkwin47
	
m1:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n1
	j checkwin47
	
n1:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o1
	j checkwin47

o1:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin47	

checkwin47:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m2
	j checkwin48
	
m2:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n2
	j checkwin48
	
n2:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o2
	j checkwin48

o2:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin48
	
checkwin48:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m3
	j checkwin49
	
m3:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n3
	j checkwin49
	
n3:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o3
	j checkwin49

o3:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin49	
	
checkwin49:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m4
	j checkwin50
	
m4:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n4
	j checkwin50
	
n4:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o4
	j checkwin50

o4:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin50
	
checkwin50:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m5
	j checkwin51
	
m5:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n5
	j checkwin51
	
n5:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o5
	j checkwin51

o5:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin51	

checkwin51:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m6
	j checkwin52
	
m6:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n6
	j checkwin52
	
n6:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o6
	j checkwin52

o6:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin52
	
checkwin52:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m7
	j checkwin53
	
m7:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n7
	j checkwin53
	
n7:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o7
	j checkwin53

o7:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin53	
	
checkwin53:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m8
	j checkwin54
	
m8:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n8
	j checkwin54
	
n8:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o8
	j checkwin54

o8:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin54

checkwin54:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m9
	j checkwin55
	
m9:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n9
	j checkwin55
	
n9:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o9
	j checkwin55

o9:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin55	

checkwin55:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m10
	j checkwin56
	
m10:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n10
	j checkwin56
	
n10:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o10
	j checkwin56

o10:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin56
	
checkwin56:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m11
	j checkwin57
	
m11:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n11
	j checkwin57
	
n11:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o11
	j checkwin57

o11:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin57	
	
checkwin57:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m12
	j checkwin58
	
m12:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n12
	j checkwin58
	
n12:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o12
	j checkwin58

o12:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin58

checkwin58:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m13
	j checkwin59

m13:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n13
	j checkwin59
	
n13:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o13
	j checkwin59
	
o13:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin59
	
checkwin59:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m14
	j checkwin60

m14:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n14
	j checkwin60
	
n14:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o14
	j checkwin60
	
o14:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin60
	
checkwin60:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m15
	j checkwin61

m15:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n15
	j checkwin61
	
n15:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o15
	j checkwin61
	
o15:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin61
	
checkwin61:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m16
	j checkwin62

m16:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n16
	j checkwin62
	
n16:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o16
	j checkwin62
	
o16:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin62
	
checkwin62:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m17
	j checkwin63

m17:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n17
	j checkwin63
	
n17:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o17
	j checkwin63
	
o17:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin63
	
checkwin63:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m18
	j checkwin64

m18:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n18
	j checkwin64
	
n18:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o18
	j checkwin64
	
o18:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin64
	
checkwin64:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m19
	j checkwin65

m19:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n19
	j checkwin65
	
n19:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o19
	j checkwin65
	
o19:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin65
	
checkwin65:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m20
	j checkwin66

m20:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n20
	j checkwin66
	
n20:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o20
	j checkwin66
	
o20:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin66

checkwin66:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m21
	j checkwin67

m21:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n21
	j checkwin67
	
n21:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o21
	j checkwin67
	
o21:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin67
	
checkwin67:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m22
	j checkwin68

m22:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n22
	j checkwin68
	
n22:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o22
	j checkwin68
	
o22:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin68
	
checkwin68:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m23
	j checkwin69

m23:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n23
	j checkwin69
	
n23:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o23
	j checkwin69
	
o23:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j checkwin69
	
checkwin69:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, m24
	j computerturncontinue

m24:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, n24
	j computerturncontinue
	
n24:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, o24
	j computerturncontinue
	
o24:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 1, won
	j computerturncontinue

victorymusic:
	li $a0, 0
	li $a1, 500
	li $a2, 0
	li $a3, 127
	li $v0, 33
	syscall
	
	li $a0, 81
	syscall
	
	li $a0, 81
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 90
	syscall
	
	li $a0, 90
	syscall
	
	li $a0, 88
	li $a1, 1000
	syscall
	
	li $a0, 86
	li $a1, 500
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 83
	syscall
	
	li $a0, 83
	syscall
	
	li $a0, 81
	li $a1, 1000
	syscall
	
	#second line
	li $a0, 88
	li $a1, 500
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 83
	li $a1, 1000
	syscall
	
	li $a0, 88
	li $a1, 500
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 83
	li $a1, 1000
	syscall
	
	#line 3 is the same as the first line
	li $a0, 81
	li $a1, 500
	syscall
	
	li $a0, 81
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 88
	syscall
	
	li $a0, 90
	syscall
	
	li $a0, 90
	syscall
	
	li $a0, 88
	li $a1, 1000
	syscall
	
	li $a0, 86
	li $a1, 500
	syscall
	
	li $a0, 86
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 85
	syscall
	
	li $a0, 83
	syscall
	
	li $a0, 83
	syscall
	
	li $a0, 81
	li $a1, 1000
	syscall
	
	jr $ra

checklose:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a
	j checklose2

a99:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, bz99
	j checklose2

bz99: 
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c99
	j checklose2

c99:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose2

checklose2:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d99
	j checklose3
	
d99:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e99
	j checklose3
	
e99:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f99
	j checklose3
	
f99:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose3

checklose3:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g99
	j checklose4
	
g99:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h99
	j checklose4

h99:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i99
	j checklose4

i99:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose4
	
checklose4:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a199
	j checklose5

a199:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b199
	j checklose5

b199: 
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c199
	j checklose5

c199:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose5

checklose5:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d199
	j checklose6
	
d199:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e199
	j checklose6
	
e199:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f199
	j checklose6
	
f199:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose6

checklose6:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g199
	j checklose7
	
g199:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h199
	j checklose7

h199:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i199
	j checklose7

i199:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose7
	
checklose7:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a299
	j checklose8

a299:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b299
	j checklose8

b299: 
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c299
	j checklose8

c299:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose8

checklose8:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d299
	j checklose9
	
d299:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e299
	j checklose9
	
e299:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f299
	j checklose9
	
f299:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose9

checklose9:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g299
	j checklose10
	
g299:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h299
	j checklose10

h299:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i299
	j checklose10

i299:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose10
	
checklose10:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a399
	j checklose11

a399:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b399
	j checklose11

b399: 
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c399
	j checklose11

c399:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose11

checklose11:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d399
	j checklose12
	
d399:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e399
	j checklose12
	
e399:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f399
	j checklose12
	
f399:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose12

checklose12:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g399
	j checklose13
	
g399:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h399
	j checklose13

h399:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i399
	j checklose13

i399:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose13
	
checklose13:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a499
	j checklose14

a499:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b499
	j checklose14

b499: 
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c499
	j checklose14

c499:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose14

checklose14:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d499
	j checklose15
	
d499:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e499
	j checklose15
	
e499:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f499
	j checklose15
	
f499:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose15

checklose15:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g499
	j checklose16
	
g499:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h499
	j checklose16

h499:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i499
	j checklose16

i499:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose16
	
checklose16:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a599
	j checklose17

a599:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b599
	j checklose17

b599: 
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c599
	j checklose17

c599:
	la $t0, row2
	
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose17

checklose17:
	la $t0, row2
	
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d599
	j checklose18
	
d599:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e599
	j checklose18
	
e599:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f599
	j checklose18
	
f599:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose18

checklose18:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g599
	j checklose19
	
g599:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h599
	j checklose19

h599:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i599
	j checklose19

i599:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose19
	
checklose19:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, a699
	j checklose20

a699:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, b699
	j checklose20

b699: 
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, c699
	j checklose20

c699:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose20

checklose20:
	la $t0, row1	
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, d699
	j checklose21
	
d699:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, e699
	j checklose21
	
e699:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, f699
	j checklose21
	
f699:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose21

checklose21:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, g699
	j checklose22
	
g699:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, h699
	j checklose22

h699:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, i699
	j checklose22

i699:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, lost
	j checklose22

checklose22:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j199
	j checklose23

j199:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k199
	j checklose23

k199:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l199
	j checklose23
	
l199:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose23
	
checklose23:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j299
	j checklose24

j299:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k299
	j checklose24

k299:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l299
	j checklose24
	
l299:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose24
	
checklose24:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j399
	j checklose25

j399:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k399
	j checklose25

k399:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l399
	j checklose25
	
l399:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose25

checklose25:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j499
	j checklose26

j499:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k499
	j checklose26

k499:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l499
	j checklose26
	
l499:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose26
	
checklose26:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j599
	j checklose27

j599:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k599
	j checklose27

k599:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l599
	j checklose27
	
l599:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose27
	
checklose27:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j699
	j checklose28

j699:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k699
	j checklose28

k699:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l699
	j checklose28
	
l699:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose28

checklose28:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j799
	j checklose29

j799:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k799
	j checklose29

k799:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l799
	j checklose29
	
l799:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose29
	
checklose29:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j899
	j checklose30

j899:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k899
	j checklose30

k899:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l899
	j checklose30
	
l899:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose30
	
checklose30:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j999
	j checklose31

j999:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k999
	j checklose31

k999:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l999
	j checklose31
	
l999:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose31

checklose31:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1099
	j checklose32

j1099:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1099
	j checklose32

k1099:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1099
	j checklose32
	
l1099:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose32
	
checklose32:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1199
	j checklose33

j1199:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1199
	j checklose33

k1199:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1199
	j checklose33
	
l1199:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose33
	
checklose33:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1299
	j checklose34

j1299:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1299
	j checklose34

k1299:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1299
	j checklose34
	
l1299:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose34
	
checklose34:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1399
	j checklose35

j1399:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1399
	j checklose35

k1399:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1399
	j checklose35
	
l1399:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose35
	
checklose35:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1499
	j checklose36

j1499:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1499
	j checklose36

k1499:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1499
	j checklose36
	
l1499:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose36
	
checklose36:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1599
	j checklose37

j1599:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1599
	j checklose37

k1599:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1599
	j checklose37
	
l1599:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose37

checklose37:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1699
	j checklose38

j1699:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1699
	j checklose38

k1699:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1699
	j checklose38
	
l1699:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose38
	
checklose38:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1799
	j checklose39

j1799:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1799
	j checklose39

k1799:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1799
	j checklose39
	
l1799:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose39
	
checklose39:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1899
	j checklose40

j1899:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1899
	j checklose40

k1899:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1899
	j checklose40
	
l1899:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose40

checklose40:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j1999
	j checklose41

j1999:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k1999
	j checklose41

k1999:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l1999
	j checklose41
	
l1999:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose41
	
checklose41:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j2099
	j checklose42

j2099:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k2099
	j checklose42

k2099:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l2099
	j checklose42
	
l2099:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose42
	
checklose42:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j2199
	j checklose43

j2199:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k2199
	j checklose43

k2199:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l2199
	j checklose43
	
l2199:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose43

checklose43:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j2299
	j checklose44

j2299:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k2299
	j checklose44

k2299:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l2299
	j checklose44
	
l2299:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose44
	
checklose44:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j2399
	j checklose45

j2399:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k2399
	j checklose45

k2399:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l2399
	j checklose45
	
l2399:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose45
	
checklose45:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, j2499
	j checklose46

j2499:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, k2499
	j checklose46

k2499:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)
	
	beq $t3, 2, l2499
	j checklose46
	
l2499:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose46 

checklose46:
	la $t0, row1
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m199
	j checklose47
	
m199:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n199
	j checklose47
	
n199:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o199
	j checklose47

o199:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose47	

checklose47:
	la $t0, row2
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m299
	j checklose48
	
m299:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n299
	j checklose48
	
n299:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o299
	j checklose48

o299:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose48
	
checklose48:
	la $t0, row3
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m399
	j checklose49
	
m399:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n399
	j checklose49
	
n399:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o399
	j checklose49

o399:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose49	
	
checklose49:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m499
	j checklose50
	
m499:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n499
	j checklose50
	
n499:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o499
	j checklose50

o499:
	la $t0, row7
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose50
	
checklose50:
	la $t0, row1
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m599
	j checklose51
	
m599:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n599
	j checklose51
	
n599:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o599
	j checklose51

o599:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose51	

checklose51:
	la $t0, row2
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m699
	j checklose52
	
m699:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n699
	j checklose52
	
n699:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o699
	j checklose52

o699:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose52
	
checklose52:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m799
	j checklose53
	
m799:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n799
	j checklose53
	
n799:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o799
	j checklose53

o799:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose53	
	
checklose53:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m899
	j checklose54
	
m899:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n899
	j checklose54
	
n899:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o899
	j checklose54

o899:
	la $t0, row7
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose54

checklose54:
	la $t0, row1
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m999
	j checklose55
	
m999:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n999
	j checklose55
	
n999:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o999
	j checklose55

o999:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose55	

checklose55:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1099
	j checklose56
	
m1099:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1099
	j checklose56
	
n1099:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1099
	j checklose56

o1099:
	la $t0, row5
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose56
	
checklose56:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1199
	j checklose57
	
m1199:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1199
	j checklose57
	
n1199:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1199
	j checklose57

o1199:
	la $t0, row6
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose57	
	
checklose57:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1299
	j checklose58
	
m1299:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1299
	j checklose58
	
n1299:
	la $t0, row6
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1299
	j checklose58

o1299:
	la $t0, row7
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose58

checklose58:
	la $t0, row1
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1399
	j checklose59

m1399:
	la $t0, row2
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1399
	j checklose59
	
n1399:
	la $t0, row3
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1399
	j checklose59
	
o1399:
	la $t0, row4
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose59
	
checklose59:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1499
	j checklose60

m1499:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1499
	j checklose60
	
n1499:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1499
	j checklose60
	
o1499:
	la $t0, row5
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose60
	
checklose60:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1599
	j checklose61

m1599:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1599
	j checklose61
	
n1599:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1599
	j checklose61
	
o1599:
	la $t0, row6
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose61
	
checklose61:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1699
	j checklose62

m1699:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1699
	j checklose62
	
n1699:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1699
	j checklose62
	
o1699:
	la $t0, row7
	li $t1, 0
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose62
	
checklose62:
	la $t0, row1
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1799
	j checklose63

m1799:
	la $t0, row2
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1799
	j checklose63
	
n1799:
	la $t0, row3
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1799
	j checklose63
	
o1799:
	la $t0, row4
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose63
	
checklose63:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1899
	j checklose64

m1899:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1899
	j checklose64
	
n1899:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1899
	j checklose64
	
o1899:
	la $t0, row5
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose64
	
checklose64:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m1999
	j checklose65

m1999:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n1999
	j checklose65
	
n1999:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o1999
	j checklose65
	
o1999:
	la $t0, row6
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose65
	
checklose65:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m2099
	j checklose66

m2099:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n2099
	j checklose66
	
n2099:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o2099
	j checklose66
	
o2099:
	la $t0, row7
	li $t1, 1
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose66

checklose66:
	la $t0, row1
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m2199
	j checklose67

m2199:
	la $t0, row2
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n2199
	j checklose67
	
n2199:
	la $t0, row3
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o2199
	j checklose67
	
o2199:
	la $t0, row4
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose67
	
checklose67:
	la $t0, row2
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m2299
	j checklose68

m2299:
	la $t0, row3
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n2299
	j checklose68
	
n2299:
	la $t0, row4
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o2299
	j checklose68
	
o2299:
	la $t0, row5
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose68
	
checklose68:
	la $t0, row3
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m2399
	j checklose69

m2399:
	la $t0, row4
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n2399
	j checklose69
	
n2399:
	la $t0, row5
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o2399
	j checklose69
	
o2399:
	la $t0, row6
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j checklose69
	
checklose69:
	la $t0, row4
	li $t1, 5
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, m2499
	j userturncontinue

m2499:
	la $t0, row5
	li $t1, 4
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, n2499
	j userturncontinue
	
n2499:
	la $t0, row6
	li $t1, 3
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, o2499
	j userturncontinue
	
o2499:
	la $t0, row7
	li $t1, 2
	sll $t1, $t1, 2
	add $t2, $t0, $t1
	lw $t3, 0($t2)

	beq $t3, 2, lost
	j userturncontinue
