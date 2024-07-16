#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% enemyMoveCell(X,Y,H,U).

% offset(-1..1).


%%AUXILIARY
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 


%%GUESS
moveIn(X,Y) | moveOut(X,Y) :- moveCell(X,Y,_).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y)} <> 1.

%%WEAK 

%
% 0
%
% need this to always get an optimal answerset
:~ moveIn(X,Y). [0@10 ]

% prefer moving to an height 3 cell 
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3. [1@10] %penalty for moving to an height != 3

%
%3
%
% avoid moving on lower cell
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-2. [3@9]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-1. [2@9]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy  . [1@9]


