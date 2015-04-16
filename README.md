# Deadlocks

This project contains the implementation of Deadlock Detection Algoritm with one resource of each type

# Instructions:
- Download the project.
- Open the project in Netbeans.
- UNZIP HERE graphviz.zip in the root directory of the project (Same level where graphviz.zip is located).
- Execute the Jframe "Window".
- Click in the button "Draw Graph".

# Inputs:
- The project uses a file called "file.txt", located in the root directory of the project.
- "file.txt" structure (without spaces between lines): 

    7   // Number of nodes.
    
    A   // We write every node.
    
    B
    
    C
    
    R
    
    S
    
    T
    
    U
    
    6   // Number of arcs.
    
    A holds S   // We write every arc.
    
    A holds U   // NOTE: "holds" means that first node is a process that holds a resource (second node).
    
    B holds R
    
    C holds T
    
    A wants R  // NOTE: "wants" means that first node is a process that wants a resource (second node).
    
    B wants T
