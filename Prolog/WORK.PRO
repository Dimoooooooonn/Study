DOMAINS
	gorod, country, continent = symbol
PREDICATES
	city(gorod,country)
	strana(country, continent)
CLAUSES
	city(london,england).
	city(liverpul,england).
	city(petersburg,russia).
	city(moscow,russia).
	city(tokio,jupan).
	city(osaka,jupan).
	city(pekin,chaina).
	city(shanghai,chaina).
	city(cairo,egypt).
	city(giza,egypt).
	
	city(X,europe):- city(X,england).
	city(X,europe):- city(X,russia).
	city(X,asia):- city(X,jupan).
	city(X,asia):- city(X,chaina).
	city(X,africa):- city(X,egypt).
	
	strana(england,europe).
	strana(russia,europe).
	strana(jupan,asia).
	strana(chaina,asia).
	strana(egypt,africa).