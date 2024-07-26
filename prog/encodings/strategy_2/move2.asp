#show moveIn/3.

%--MOVE------------------------------------------------------------------------------

%%FACTS - added from Group.java
% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% choosedUnit(U). --> in case of 2 units per player.
% moveCell(X,Y,H). --> cell where I can move unit 
% enemyMoveCell(X,Y,H,U).

os(-1..1).

%%AUXILIARY
myUnit(X,Y,H,U):- cell(X,Y,H,U), choosedUnit(U). 
validCell(X,Y,H,U) :-cell(X,Y,H,U), X>=0, Y>=0, H<>4.
enemyUnit(X,Y,H,U):- cell(X,Y,H,U), myUnit(_,_,_,Umy), U<>Umy , U<>-1. %non funziona con piu' unita' per giocatore

offset(X,Y) :- os(X), os(Y), &abs(X;Xabs), &abs(Y;Yabs), &sum(Xabs,Yabs;Z), Z<>0. % offset can't be 0,0

nearEnemyMoveCell(Xnear,Ynear,Hnear,X,Y,H) :- enemyMoveCell(X,Y,H,U), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,Hnear,_). % cells near enemy moveCell
nearMoveCell(Xnear,Ynear,Hnear,X,Y,H):- moveCell(X,Y,H), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validCell(Xnear,Ynear,Hnear,_).


%%GUESS
moveIn(X,Y,H) | moveOut(X,Y) :- moveCell(X,Y,H).

%%CHECK
% can choose only one cell
:- #count{X,Y : moveIn(X,Y,_)} <> 1.


%%WEAK 

% need this to always get an optimal answerset
:~ moveIn(X,Y,H). [0@10]

% 1)prefer moving to an height 3 cell 
:~ moveIn(X,Y,H), H<>3. [1@10] %penalty for moving to an height != 3


% 2) prefer moving to a cell from where myUnit can block enemyUnit to win 

% if enemyUnit can win next turn, move to a cell from where myUnit can block enemyUnit to win 
block1(X,Y) :- nearEnemyMoveCell(X,Y,_,_,_,3), moveCell(X,Y,_) . % cells from where myUnit can block enemyUnit to win
:~ moveOut(X,Y), block1(X,Y).  [1@9, X,Y]  

% if enemyUnit can win in the next 2 turns, move to a cell from where myUnit can block  
block2(X,Y) :- nearEnemyMoveCell(X,Y,_,_,_,2), moveCell(X,Y,_) . % cells from where myUnit can block enemyUnit to win
:~ moveOut(X,Y), block2(X,Y).  [1@8, X,Y]
block3(Xnear,Ynear) :- nearEnemyMoveCell(X,Y,_,_,_,2), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), moveCell(Xnear,Ynear,Hnear) . % cells from where myUnit can block enemyUnit to win
:~ moveOut(X,Y), block3(X,Y).  [1@7, X,Y]


% 3)prefer moving to a height 2 cell if the cell is near an height 3 ->
:~ not moveIn(X,Y,2), nearMoveCell(X,Y,3,_,_,2), moveCell(X,Y,2). [1@6, X,Y] % penalty if exist an height 3 cell near a moveCell and don't move to moveCell  

% 4)prefer moving to an height 2 cell if the cell is near an height 2
:~ not moveIn(X,Y,2), nearMoveCell(X,Y,2,_,_,2), moveCell(X,Y,2). [1@5, X,Y] % penalty if exist an height 2 cell near a moveCell and don't move to moveCell

% 5)prefer moving on higher cell -> this weak can be useful for the enemy to win in certain situations -"assist move"- (limitation for this strategy)
:~ moveIn(X,Y,H), myUnit(_,_,Hmy,_), H<=Hmy, &sum(H,-Hmy;Z), &abs(Z;W). [W+1@4] 
%%%AGGRESSIVE STRATEGY
:~ moveIn(X,Y,H), not enemyMoveCell(X,Y,H). [2@2] % penalty if don't move to an enemyMoveCell
:~ moveIn(X,Y,H), not nearEnemyMoveCell(X,Y,H,Xm,Ym,Hm), moveCell(Xm,Ym,Hm). [1@2] % penalty if don't move to a nearEnemyMoveCell

