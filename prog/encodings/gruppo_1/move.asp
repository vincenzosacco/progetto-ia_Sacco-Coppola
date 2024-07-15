#show moveIn/2.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,P). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, P is the player code that is in the cellgrid (-1 if no player)
% unit(X,Y,H,U,P). --> represent a unit. X,Y coordinates |H height |U unitCode | P playerCode
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% enemyMoveCell(X,Y,H,U).

os(-1..1).
maxRow(4).
maxCol(4).

%%AUXILIARY
offset(X,Y) :- os(X), os(Y), &abs(X;Xabs), &abs(Y;Yabs), &sum(Xabs,Yabs;Z), Z<>0. % offset can't be 0,0
myUnit(X,Y,H,U):- unit(X,Y,H,U,_), choosedUnit(U). 
friendUnit(X,Y,H,U):- unit(X,Y,H,U,P), player(P).
enemyUnit(X,Y,H,U):- unit(X,Y,H,U,Penemy), player(P), Penemy<>P.
validCell(X,Y,H,P) :-cell(X,Y,H,P), H>=0, H<4, X>=0, Y>=0, X<=Xmax, Y<= Ymax, maxRow(Xmax), maxCol(Ymax).

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

%
% 1
%
% prefer moving to an height 3 cell 
:~ moveIn(X,Y), moveCell(X,Y,H), H<>3. [1@10] %penalty for moving to an height != 3

%
% 2
%
% prefer moving to a cell from where myUnit can block enemyUnit to win 
nearEnemyMoveCell3(Xnear,Ynear) :- enemyMoveCell(X,Y,3,U), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,_,_).

:~ not moveIn(X,Y), moveCell(X,Y,_), nearEnemyMoveCell3(X,Y).  [1@9, X,Y]   % penalty if move to a cell where myUnit can't build on enemyMoveCell(X,Y,3,_)  


%
% 3
%
% prefer moving to a height 2 cell if the cell is near an height 3 ->
h2_nearMoveCell3(Xnear,Ynear,X,Y):- validCell(X,Y,3,_), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,_,_).

:~ not moveIn(X,Y), moveCell(X,Y,_), h2_nearMoveCell3(X,Y,_,_). [1@8, X,Y] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  

%
%4
%
% prefer moving on higher cell
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy  . [1@7]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-1. [2@7]
:~ moveIn(X,Y), moveCell(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-2. [3@7]

%
%5
%
% prefer moving near the enemy
% :~ moveIn(X,Y), moveCell(X,Y,H), enemyMoveCell(X,Y,Henemy,_), &sum(H,-Henemy;Z), Z>0. [Z@6, X,Y]