% cell(X,Y,H,U). --> a cell of the grid --> X,Y are the coordinates of the cell, H is the height of the cell, U is the unitCode of the unit on the cell (-1 if no unit)

gridCell(X,Y,H):- cell(X,Y,H,_).
unit(X,Y,U):- cell(X,Y,_,U), U<>-1.

#show gridCell/3.
#show unit/3.