#show buildIn/2.

% --BUILD----------------------------------------------------------------------------

%%FACTS
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cell (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% buildCell(X,Y,H). --> cell where I can build
% enemyMoveCell(X,Y,H,U).

% AUXILIARY
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 
friendUnit(X,Y,H,U):- unit(X,Y,H,U,P), player(P).
enemyUnit(X,Y,H,U):- unit(X,Y,H,U,Penemy), player(P), Penemy<>P.


% GUESS
buildIn(X,Y) | buildOut(X,Y) :- buildCell(X,Y,_).

% CHECK
% can build on only one cell
:- #count{X,Y : buildIn(X,Y)} <> 1.

% WEAK -10 

%
%0
%
% need this to always get an optimal answerset
:~ buildIn(X,Y). [0@10]

%
%1
%
% prefer build (height 4) on a buildable height 3 cell near the enemy
:~ not buildIn(X2,Y2), enemyMoveCell(X2,Y2,3,_), buildCell(X2,Y2,3). [1@10] % penalize if exist a buildAble enemyMoveCell(X,Y,3,_) and don't build on it 

%
%2
%
% avoid build height 4 near myUnit 
:~ buildIn(X,Y), buildCell(X,Y,3). [1@9] 


%
%1
%
% near enemy preferences

% avoid enemy climbing
:~ buildIn(X,Y),enemyMoveCell(X,Y,H,_), enemyUnit(_,_,Henemy,_), &sum(H+1,-Henemy;Z), Z=1. [3@7] 
:~ buildIn(X,Y),enemyMoveCell(X,Y,H,_), enemyUnit(_,_,Henemy,_), &sum(H+1,-Henemy;Z), Z=0. [2@7] 
:~ buildIn(X,Y),enemyMoveCell(X,Y,H,_), enemyUnit(_,_,Henemy,_), &sum(H+1,-Henemy;Z), Z=2. [1@7] 
:~ buildIn(X,Y),enemyMoveCell(X,Y,H,_), enemyUnit(_,_,Henemy,_), &sum(H+1,-Henemy;Z), Z=3. [0@7] 

%
%2
%


%
%3
%
% prefer myUnit climbing
% :~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z=0. [1@7]
% :~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z>1. [Z@7]

% Z = 1 -> best build
:~ buildIn(X,Y), buildCell(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z<>1. [1@7] 



%cell(0,0,0,-1). cell(0,1,0,-1). cell(0,2,0,-1). cell(0,3,0,-1). cell(0,4,0,-1). cell(1,0,0,-1). cell(1,1,0,-1). cell(1,2,0,-1). cell(1,3,0,-1). cell(1,4,0,1). cell(2,0,0,-1). cell(2,1,1,0). cell(2,2,0,-1). cell(2,3,1,-1). cell(2,4,0,-1). cell(3,0,0,-1). cell(3,1,0,-1). cell(3,2,0,-1). cell(3,3,0,-1). cell(3,4,0,-1). cell(4,0,0,-1). cell(4,1,0,-1). cell(4,2,0,-1). cell(4,3,0,-1). cell(4,4,0,-1). player(0). unit(2,1,1,1,0). unit(1,4,0,2,1). choosedUnit(1). enemyMoveCell(0,4,0,2). enemyMoveCell(2,4,0,2). enemyMoveCell(2,3,1,2). enemyMoveCell(1,3,0,2). enemyMoveCell(0,3,0,2). buildCell(1,1,0). buildCell(1,2,0). buildCell(2,2,0). buildCell(3,2,0). buildCell(3,1,0). buildCell(3,0,0). buildCell(2,0,0). buildCell(1,0,0).