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
validCell(X,Y,H,U) :-cell(X,Y,H,U), X>=0, Y>=0, H<>4.


% GUESS
buildIn(X,Y,H) | buildOut(X,Y) :- buildCell(X,Y,H).

% CHECK
% can build on only one cell
:- #count{X,Y : buildIn(X,Y,_)} <> 1.

% WEAK -10

% need this to always get an optimal answerset
:~ buildIn(X,Y,H). [0@10]


% prefer build (height 4) on a buildable height 3 cell near the enemy
:~ not buildIn(X,Y,3), enemyMoveCell(X,Y,3,_). [1@9, X,Y] % penalize if exist a buildAble enemyMoveCell(X,Y,3,_) and don't build on it



% avoid enemy climbing
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=1. [3@8]
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=0. [2@8]
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,U), enemyUnit(_,_,Henemy,U), &sum(H+1,-Henemy;Z), Z=2. [1@8]

% avoid build height 4 near myUnit
:~ buildIn(X,Y,3). [1@7]


% prefer myUnit climbing
:~ buildIn(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z=0. [1@6]
:~ buildIn(X,Y,H), myUnit(_,_,Hmy,_), &sum(H+1,-Hmy;Z), Z<>1, &abs(Z;W). [W+1@6]

% avoid build near enemyUnit
:~ buildIn(X,Y,H), enemyMoveCell(X,Y,H,_). [1@5]

% prefer build near higher cells
maxHeight(H):- #max{H1 : cell(_,_,H1,_)} = H.
higherCell(X,Y):- cell(X,Y,H,_), maxHeight(H).
nearHigherCell(X,Y):- higherCell(X,Y), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,_,_).

:~ buildOut(X,Y), nearHigherCell(X,Y). [1@5, X,Y] 

