% Questo encoding ha come scopo quello di generare tutti i possibili stati in cui giocatore si può trovare dopo un'azione.


#show moveIn/3.  % cella in cui l'unità si è mossa
#show buildIn/3. % cella in cui l'unità ha costruito

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%FACTS
% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% moveCell(X,Y,H). --> cell where I can move unit
% unit(U) -> unit that perfoms the action

%%AUXILIARY
os(-1..1).
offset(X,Y) :- os(X), os(Y), &abs(X;Xabs), &abs(Y;Yabs), &sum(Xabs,Yabs;Z), Z<>0. % offset can't be 0,0

%%%%MOVE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

moveIn(X,Y,H) | moveOut(X,Y) :- moveCell(X,Y,H).
moveIn(-1,-1,-1) :- #count{X,Y : moveCell(X,Y,_)} = 0 . % if there are no moveable cells, then moveIn(-1,-1,-1) is true (i.e., lose the game

% can choose only one cell
:- #count{X,Y : moveIn(X,Y,H)} <> 1.

%%%%BUILD%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

validBuild(X,Y,H) :-cell(X,Y,H,U), H<4, U=-1.
buildCell(Xnear,Ynear,Hnear):- moveIn(X,Y,_), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validBuild(Xnear,Ynear,Hnear).

%%GUESS
buildIn(X,Y,H) | buildOut(X,Y) :- buildCell(X,Y,H1), H = H1+1.
buildIn(-1,-1,-1) :- #count{X,Y : buildCell(X,Y,_)} = 0 . % if there are no buildable cells, then buildIn(-1,-1,-1) is true (i.e., lose the game)

%%CHECK
% can choose only one cell
:- #count{X,Y : buildIn(X,Y,H)} <> 1.


% AFTER UNIT PERFORMS ACTION, CALCULATE VALUE OF THE NEW STATE
%%%%VALUE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%4
% Questo encoding da un valore intero ad una mossa del gioco, un valore più alto vuol dire mossa migliore. 

#show value/1.

% Generate new cells
newCell(X,Y,H,U):- cell(X,Y,H,U), not buildIn(X,Y,H), not moveIn(X,Y,H). % moveIn is not considered
newCell(X,Y,H,U):- cell(X,Y,H,Uold), unit(Uold), U = -1. % unit is moved
newCell(X,Y,Hbuild,U):- cell(X,Y,H,U), buildIn(X,Y,Hbuild) . % builded cell is increased by 1
% #show newCell/4.

% Generate new move cells
validMove(X,Y,H):- newCell(X,Y,H,U), H<4, U=-1, moveIn(Xm,Ym,Hm), &sum(H,-Hm ; L), L<2 . 
newMoveCell(Xnear,Ynear,Hnear):- moveIn(X,Y,_), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validMove(Xnear,Ynear,Hnear).
% #show newMoveCell/3.

highestMoveCell(H):- #max{H1 : newMoveCell(X,Y,H1)} = H.


value(V):- moveIn(_,_,H), highestMoveCell(H1), &sum(H,H1;V).


% cell(0,0,0,-1). cell(0,1,0,-1). cell(0,2,0,-1). cell(0,3,0,-1). cell(0,4,0,0). cell(1,0,0,-1). cell(1,1,0,-1). cell(1,2,0,-1). cell(1,3,0,-1). cell(1,4,0,-1). cell(2,0,0,-1). cell(2,1,0,-1). cell(2,2,0,-1). cell(2,3,0,-1). cell(2,4,0,-1). cell(3,0,0,-1). cell(3,1,0,-1). cell(3,2,0,-1). cell(3,3,0,-1). cell(3,4,0,-1). cell(4,0,0,-1). cell(4,1,0,-1). cell(4,2,0,1). cell(4,3,0,-1). cell(4,4,0,-1). moveCell(0,3,0). moveCell(1,3,0). moveCell(1,4,0).