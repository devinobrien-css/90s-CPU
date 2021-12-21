# 90s-CPU

Hello there!👋

Devin here; This repository holds my code for a project from an Assembly and Computer Architecture course I took at University.


**Overarching Project Goal**

These classes form an elemental example of the software behind 90s computer architecture. The Object Oriented CPU engine I've built takes in BASIC-based assembly language as input; Said input is lexed, parsed, and semantically analyzed prior to being converted to binary. The bitcode is then stored in the machine's virtual memory space.



**Notable Aspects and Learning Points**

There are several distinguishable aspects within this project worth noting.



**Running the Code**

*Input* : Assembly language on file written in a variant of BASIC
*Intermediary* : -> Lexical Analysis -> Binary Stream -> Executable ->
*Output* : Execute 

*Execution* :
- launch a terminal/shell, change working directory to `CPU`, run shell commands:
    javac *.java
    java Driver
  
The program will read from the asssembly file `input.txt` and execute accordingly. 
