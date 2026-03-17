DOMAINS
    person = symbol

PREDICATES
    parent(person, person).
    cousin(person, person).
    grandfather(person, person).
    grandmother(person, person).
    uncle(person, person).
    sibling(person, person).
    male(person).
    female(person).
    shurin(person, person).
    jenat(person, person).
    dever(person, person).

CLAUSES
    parent(petr, maksim).
    parent(elena, maksim).
    parent(petr, olesya).
    parent(elena, olesya).

    parent(leonid, evgeniy).
    parent(nina, evgeniy).
    parent(leonid, sergey).
    parent(nina, sergey).

    parent(olesya, dmitry).
    parent(evgeniy, dmitry).
    parent(olesya, polina).
    parent(evgeniy, polina).

    parent(sergey, danil).
    parent(sergey, aleksei).
    parent(sergey, miroslava).


    male(dmitry).
    male(evgeniy).
    male(maksim).
    male(sergey).
    male(danil).
    male(aleksei).
    male(petr).
    male(leonid).
    female(olesya).
    female(polina).
    female(miroslava).
    female(elena).
    female(nina).


    sibling(X,Y) :-
        parent(P, X),
        parent(P, Y),
        X <> Y.

    grandfather(G, X) :-
        parent(G, P),
        parent(P, X),
        male(G). 
   
    grandmother(G, X) :-
        parent(G, P),
        parent(P, X),
        female(G).

    
    uncle(maksim, dmitry).
    uncle(sergey, dmitry).


    uncle(X, Y) :-
        parent(P, Y),
        cousin(P, X),
        male(X).

    
    cousin(danil, dmitry).
    cousin(aleksei, dmitry).
    cousin(miroslava, dmitry).

    jenat(evgeniy, olesya).
    jenat(petr, elena).
    jenat(leonid, nina).

    shurin(X, Y) :- 
        sibling(X, H),
        jenat(Y, H),
        male(X), !.

   dever(X, Y) :-
        sibling(G, X),
        jenat(G, Y),
        male(X), !.   
