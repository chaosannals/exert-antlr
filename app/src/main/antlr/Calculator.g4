grammar Calculator;

@header {
    package exert.antlr.grammar;
}


opc: NUM
    | PL opc PR
    | opc op=(STAR|SLASH) opc
    | opc op=(PLUS|MINUS) opc
    ;


PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
PL: '(';
PR: ')';
NUM: [0-9]+('.'[0-9]+)? ;
WS: [ \t\r\n]+ -> skip;
