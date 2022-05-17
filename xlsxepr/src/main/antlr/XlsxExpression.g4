grammar XlsxExpression;

@header {
    package exert.antlr.xlsxepr.grammar;
}

total: EQ expression;

expression
    : FUNCNAME values PR #func
    | PL expression PR #expressionP
    | value #expressionValue
    | expression op=(STAR|SLASH) expression #mulAndDiv
    | expression op=(PLUS|MINUS) expression #addAndSub
    ;

value
    : NUMBER #numberValue
    | STRING #stringValue
    | LOCATION #rangeSingle
    | ROWS #rangeRows
    | COLUMNS #rangeColums
    | LOCATION COLON LOCATION #range
    ;

values
    : #empty
    | expression #valueSingle
    | expression COMMA values #valueList
    ;


EQ: '=';
PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
COMMA: ',';
COLON: ':';
PL: '(';
PR: ')';
ROWS: DIGITS':'DIGITS;
STRING: '"' (ESC | .)*? '"';
NUMBER: INT ('.' DIGITS)? ([Ee][+-]? DIGITS)?;
COLUMNS: [A-Z]+':'[A-Z]+;
LOCATION: [A-Z]+[0-9]+;
FUNCNAME: [A-Za-z][0-9A-Za-z]*'(' ;

WS: [ \t\r\n] -> skip;

fragment ESC: '\\' ([bftnr"\\] | 'u' HEX HEX HEX HEX);
fragment HEX: [0-9A-Fa-f];
fragment DIGITS: ([0-9] | [1-9][0-9]+);
fragment INT: '-'? DIGITS;