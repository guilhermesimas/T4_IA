:- dynamic (lim/2).
:- dynamic (ouro/2).  
:- dynamic (powerup/2).
:- dynamic (blocked/2).
:- dynamic (buraco/2).
:- dynamic (teleport/2).
:- dynamic (notp/3).
:- dynamic (possivelp/3).
:- dynamic (desconhecido/2).
:- dynamic (visitado/2).
:- dynamic (modified/3).




init():-
    assert(lim(59,34)),
    retractall(possivelp(_,_,_)),
    retractall(ouro(_,_)),
    retractall(powerup(_,_)),
    retractall(buraco(_,_)),
    retractall(teleport(_,_)),
    retractall(notp(_,_,_)),
    retractall(desconhecido(_,_)),
    retractall(visitado(_,_)).

direita(norte,leste).
direita(leste,sul).
direita(sul,oeste).
direita(oeste,norte).

esquerda(norte,oeste).
esquerda(oeste,sul).
esquerda(sul,leste).
esquerda(leste,norte).

adj(X,Y,norte,X,Y2):- Y>0, Y2 is Y-1.
adj(X,Y,sul,X,Y2):- lim(_,LIM),Y<LIM-1, Y2 is Y+1.
adj(X,Y,leste,X2,Y):- lim(LIM,_), X<LIM-1, X2 is X+1.
adj(X,Y,oeste,X2,Y):- X>0, X2 is X-1.


set1(X,Y,buraco):- 
    not(buraco(X,Y)),
    assert(buraco(X,Y)),
    assert(modified(X,Y,buraco)),
    X2 is X+2, X3 is X-2,Y2 is Y-2, Y3 is Y+2, X4 is X+1, Y4 is Y+1,Y5 is Y-1, X5 is X-1,
    (setp(buraco,X2,Y,notp);
        setp(buraco,X3,Y,notp);
        setp(buraco,X,Y2,notp);
        setp(buraco,X,Y3,notp);
        setp(buraco,X4,Y4,notp);
        setp(buraco,X4,Y5,notp);
        setp(buraco,X5,Y4,notp);
        setp(buraco,X5,Y5,notp); 
        setp(teleport,X,Y,notp)).


set1(X,Y,teleport):-
    not(teleport(X,Y)),
    assert(teleport(X,Y)),
    assert(modified(X,Y,teleport)),
   X2 is X+2, X3 is X-2,Y2 is Y-2, Y3 is Y+2, X4 is X+1, Y4 is Y+1,Y5 is Y-1, X5 is X-1,
   ( setp(teleport,X2,Y,notp);
       setp(teleport,X3,Y,notp);
       setp(teleport,X,Y2,notp);
       setp(teleport,X,Y3,notp);
       setp(teleport,X4,Y4,notp);
       setp(teleport,X4,Y5,notp);
       setp(teleport,X5,Y4,notp);
       setp(teleport,X5,Y5,notp); 
       setp(buraco,X,Y,notp)).


setp(Nome,X,Y,possivelp):-not(possivelp(Nome,X,Y)),assert(possivelp(Nome,X,Y)),S = possivel, string_concat(S,Nome,S2),assert(modified(X,Y,S2)).
setp(Nome,X,Y,notp):-not(notp(Nome,X,Y)),assert(notp(Nome,X,Y)).

set(X,Y,visitado):-not(visitado(X,Y)),assert(visitado(X,Y)),assert(modified(X,Y,visitado)).
set(X,Y,powerup):- not(powerup(X,Y)),assert(powerup(X,Y)),assert(modified(X,Y,powerup)).
set(X,Y,ouro):- not(ouro(X,Y)),assert(ouro(X,Y)),assert(modified(X,Y,ouro)).
set(X,Y,blocked):- not(blocked(X,Y)),assert(blocked(X,Y)),assert(modified(X,Y,blocked)).


perigo(X,Y):- buraco(X,Y); teleport(X,Y).
safe(X,Y):-notp(buraco,X,Y),notp(teleport,X,Y).

possivelperigo(X,Y):- possivelp(buraco,X,Y); possivelp(teleport,X,Y).


brisa(X,Y):- updateMemP(X,Y,buraco).
notbrisa(X,Y):- updateMemSafe(X,Y,buraco).

flash(X,Y):- updateMemP(X,Y,teleport).
notflash(X,Y):- updateMemSafe(X,Y,teleport).


updateMemSafebackup(X,Y,Nome):- % marca todas as adjacencias como nao perigo
    adj(X,Y,_,X2,Y2),
    (setp(Nome,X2,Y2,notp) ; retract(possivelp(Nome,X2,Y2))).
    
updateMemSafe(X,Y,Nome):- % marca todas as adjacencias como nao perigo
    adj(X,Y,_,X2,Y2),
    setp(Nome,X2,Y2,notp),
    retiraperigo(Nome,X2,Y2).

retiraperigo(Nome,X,Y):-retract(possivelp(Nome,X,Y)).
retiraperigo(_,_,_).


updateMemP(X,Y,Nome):- % marca as adjacencias que nao foram visitadas ou nao sao "nao perigo" como possiveis perigos, e entao checa se hÃ¡ mais de uma
    adj(X,Y,_,X2,Y2),
    not(visitado(X2,Y2)),
    not(notp(Nome,X2,Y2)),
    setp(Nome,X2,Y2,possivelp).

countp(Nome,X,Y,C4):-
    C is 0, 
    (((adj(X,Y,norte,X2,Y2),possivelp(Nome,X2,Y2)),C1 is C+1,!); C1=C),
    (((adj(X,Y,sul,X3,Y3),possivelp(Nome,X3,Y3)),C2 is C1+1,!); C2=C1),
    (((adj(X,Y,leste,X4,Y4),possivelp(Nome,X4,Y4)),C3 is C2+1,!); C3=C2),
    (((adj(X,Y,oeste,X5,Y5),possivelp(Nome,X5,Y5)),C4 is C3+1,!); C4=C3).
	
countnp(Nome,X,Y,C4):- 
    C is 0, 
    (((adj(X,Y,norte,X2,Y2),notp(Nome,X2,Y2)),C1 is C+1,!); C1=C),
    (((adj(X,Y,sul,X3,Y3),notp(Nome,X3,Y3)),C2 is C1+1,!); C2=C1),
    (((adj(X,Y,leste,X4,Y4),notp(Nome,X4,Y4)),C3 is C2+1,!); C3=C2),
    (((adj(X,Y,oeste,X5,Y5),notp(Nome,X5,Y5)),C4 is C3+1,!); C4=C3).


checkperigo(Nome,X,Y):- % se houver apenas um possivel perigo, marca como perigo
    countp(Nome,X,Y,C),
    C = 1,
    countnp(Nome,X,Y,C1),
    C1 = 3,
    adj(X,Y,_,X2,Y2),
    possivelp(Nome,X2,Y2),
    set1(X2,Y2,Nome).
    

sugestao(X,Y,_,R):- 
    ouro(X,Y),
    retract(ouro(X,Y)),
    R=pegar_ouro,
    !.
    
sugestao(X,Y,_,R):- 
    powerup(X,Y),
    retract(powerup(X,Y)),
    R=pegar_powerup,
    !.

sugestao(X,Y,D,R):-
    adj(X,Y,D,X2,Y2),
    safe(X2,Y2),
    not(blocked(X2,Y2)),
    not(visitado(X2,Y2)),
    R=andar,
    !.

sugestao(X,Y,D,R):-
    direita(D,D2),
    adj(X,Y,D2,X2,Y2),
    safe(X2,Y2),
    not(blocked(X2,Y2)),
    not(visitado(X2,Y2)),
    R=virar_direita,
    !.
    
sugestao(X,Y,D,R):-
    direita(D,D3),
    direita(D3,D2),
    adj(X,Y,D2,X2,Y2),
    safe(X2,Y2),
    not(blocked(X2,Y2)),
    not(visitado(X2,Y2)),
    R=virar_direita,
    !.
    
sugestao(X,Y,D,R):-
    esquerda(D,D2),
    adj(X,Y,D2,X2,Y2),
    safe(X2,Y2),
    not(blocked(X2,Y2)),
    not(visitado(X2,Y2)),
    R=virar_esquerda,
    !.

sugestao(_,_,_,R):-
    safe(X,Y),
    not(visitado(X,Y)),
    R=astar_safe,!.

sugestao(_,_,_,R):-
   safe(X,Y),
   not(blocked(X,Y)),
   R=coletar_ouros,!.

sugestao(X,Y,D,R):-
    adj(X,Y,D,X2,Y2),
    not(blocked(X2,Y2)),
    R=andar,
    !.
    
sugestao(_,_,_,R):-
    R=virar_direita,
    !.
    
    