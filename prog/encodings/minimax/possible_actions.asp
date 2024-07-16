% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% moveCell(X,Y,H). --> cell where I can move unit
#show moveIn/3.
#show buildIn/3.
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
os(-1..1).
maxRow(4).
maxCol(4).

%%AUXILIARY
offset(X,Y) :- os(X), os(Y), &abs(X;Xabs), &abs(Y;Yabs), &sum(Xabs,Yabs;Z), Z<>0. % offset can't be 0,0
validBuild(X,Y,H) :-cell(X,Y,H,U), H<4, U=-1.

%%%%MOVE%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

moveIn(X,Y,H) | moveOut(X,Y) :- moveCell(X,Y,H).
moveIn(-1,-1,-1) :- #count{X,Y : moveCell(X,Y,_)} = 0 . % if there are no moveable cells, then moveIn(-1,-1,-1) is true (i.e., lose the game

% can choose only one cell
:- #count{X,Y : moveIn(X,Y,H)} <> 1.

%%%%BUILD%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

buildCell(Xnear,Ynear,Hnear):- moveIn(X,Y,_), offset(OffX,OffY), &sum(X,OffX;Xnear), &sum(Y,OffY;Ynear), validBuild(Xnear,Ynear,Hnear).

%%GUESS
buildIn(X,Y,H) | buildOut(X,Y) :- buildCell(X,Y,H1), H=H1+1.
buildIn(-1,-1,-1) :- #count{X,Y : buildCell(X,Y,_)} = 0 . % if there are no buildable cells, then buildIn(-1,-1,-1) is true (i.e., lose the game)

%%CHECK
% can choose only one cell
:- #count{X,Y : buildIn(X,Y,H)} <> 1.

