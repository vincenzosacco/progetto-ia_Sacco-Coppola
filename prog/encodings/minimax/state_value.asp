% Questo encoding da un valore intero ad una mossa del gioco, un valore più alto vuol dire mossa migliore. 

%%FACTS
% moveIn(X,Y,H) -> X,Y are the coordinates of the cell, H is the height of the cell
% buildIn(X,Y,H) -> X,Y are the coordinates of the cell, H is the height of the cell


%%AUXILIARY

value(N):- moveIn(_,_,N).