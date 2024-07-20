#show buildIn/3.

% --BUILD----------------------------------------------------------------------------

%%FACTS
% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% choosedUnit(U). --> in case of 2 units per player.
% buildCell(X,Y,H). --> cell where I can build
% enemyMoveCell(X,Y,H,U).

% AUXILIARY
myUnit(X,Y,H,U):- cell(X,Y,H,U), choosedUnit(U). 
enemyUnit(X,Y,H,U):- cell(X,Y,H,U), myUnit(_,_,_,Umy), U<>Umy , U<>-1. %non funziona con piu' unita' per giocatore


% GUESS
buildIn(X,Y,H) | buildOut(X,Y) :- buildCell(X,Y,H).

% CHECK
% can build on only one cell
:- #count{X,Y : buildIn(X,Y,_)} <> 1.

% WEAK -10 

%
%0
%
% need this to always get an optimal answerset
:~ buildIn(X,Y,H). [0@10]

%
%1
%
% prefer build (height 4) on a buildable height 3 cell near the enemy
:~ not buildIn(X,Y,3), enemyMoveCell(X,Y,3,_). [1@9, X,Y] % penalize if exist a buildAble enemyMoveCell(X,Y,3,_) and don't build on it 

%
%2
%
% avoid enemy climbing
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=1. [3@8] 
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=0. [2@8] 
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=2. [1@8] 

#show enemyMoveCell/4.
%
%3
%
% avoid build height 4 near myUnit 
:~ buildIn(X,Y,3). [1@7] 

%
%4
%
% prefer myUnit climbing
:~ buildIn(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z=0. [1@6]
:~ buildIn(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z>1. [Z@6]



