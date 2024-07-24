% Dato un grafo MinMax "mozzato", restituisce il valore della mossa migliore per il giocatore corrente.

%%FACTS
%vertex(ID,T,Value). --> vertex of the graph --> ID is the unique identifier of the vertex; T is type {MAX,MIN,LEAF,ROOT} ; Value is the value of the vertex
%edge(V1,V2). --> edge of the graph --> V1 is the source vertex ID, V2 is the destination vertex ID

% #show best/1.

% %%AUXILIARY
% min_v(ID) :- #max{Value : vertex(ID,"MIN",Value)} = V, vertex(ID,"MIN",V).

% %%GUESS
% best(Max) :- vertex(Max,"MAX",_), ma edge(Max,).


