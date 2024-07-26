% Questo encoding da un valore intero ad una mossa del gioco, un valore più alto vuol dire mossa migliore. 

% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)
% unit(U) -> unit that perfomerd the action 
% moveCell(X,Y,H). --> cell where I can move unit

#show value/1.

myUnit(X,Y,H):- cell(X,Y,H,U), unit(U).

value(V):- myUnit(_,_,V).


