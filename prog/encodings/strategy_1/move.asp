#show moveIn/3.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% enemyMoveCell(X,Y,H,U).

os(-1..1).

%%AUXILIARY
offset(X,Y) :- os(X), os(Y), &abs(X;Xabs), &abs(Y;Yabs), &sum(Xabs,Yabs;Z), Z<>0. % offset can't be 0,0
myUnit(X,Y,H,U):- cell(X,Y,H,U), choosedUnit(U). 
validCell(X,Y,H,U) :-cell(X,Y,H,U), X>=0, Y>=0.

%%GUESS
moveIn(X,Y,H) | moveOut(X,Y) :- moveCell(X,Y,H).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y,_)} <> 1.


%%WEAK 

% need this to always get an optimal answerset
:~ moveIn(X,Y,H). [0@10 ]

% prefer moving to an height 3 cell 
:~ moveIn(X,Y,H), H<>3. [1@10] %penalty for moving to an height != 3


% prefer moving to a cell from where myUnit can block enemyUnit to win 
nearEnemyMoveCell_h3(Xnear,Ynear) :- enemyMoveCell(X,Y,3,U), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,_,_). % cells near enemy moveCell
blockEnemyCell(X,Y) :- nearEnemyMoveCell_h3(X,Y), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), moveCell(Xnear,Ynear,_) . % cells from where myUnit can block enemyUnit to win

:~ moveOut(X,Y), blockEnemyCell(X,Y).  [1@9, X,Y]   % penalty if exist a cell from where myUnit can block enemyUnit to win and don't move to it

% prefer moving to a height 2 cell if the cell is near an height 3 ->
h2_nearMoveCell3(Xnear,Ynear,X,Y):- validCell(X,Y,3,_), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), moveCell(X,Y,H).

:~ not moveIn(X,Y,H), moveCell(X,Y,H), h2_nearMoveCell3(X,Y,_,_). [1@8, X,Y] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  


% prefer moving on higher cell -> this weak can be useful for the enemy to win in certain situations -"assist move"- (limitation for this strategy)
:~ moveIn(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy  . [1@7] 
:~ moveIn(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-1. [2@7]
:~ moveIn(X,Y,H), myUnit(_,_,Hmy,_), H=Hmy-2. [3@7]


