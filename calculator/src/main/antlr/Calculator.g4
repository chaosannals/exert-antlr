grammar Calculator;

@header {
    package exert.antlr.grammar;
}


opc: NUM #asNumber
    | PL opc PR #asPrior
    | opc op=(STAR|SLASH) opc #asLv2
    | opc op=(PLUS|MINUS) opc #asLv1
    ;


PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
PL: '(';
PR: ')';
NUM: [0-9]+('.'[0-9]+)? ;
WS: [ \t\r\n]+ -> skip;
