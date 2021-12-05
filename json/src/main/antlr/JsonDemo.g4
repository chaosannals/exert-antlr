grammar JsonDemo;

@header {
    package exert.antlr.json.grammar;
}

value:
	object		# objectValue
	| array		# arrayValue
	| STRING	# stringValue
	| NUMBER	# numberValue
	| TRUE		# boolValue
	| FALSE		# boolValue
	| NULL		# nullValue;

object: CL CR # voidObject | CL members CR # normalObject;

members:
	member					# uniqueMember
	| member COMMA members	# multipleMember;

member: STRING COLON value;

array: BL BR # voidArray | BL values BR # normalArray;

values: value # uniqueValue | value COMMA values # multipleValue;

PLUS: '+';
MINUS: '-';
COMMA: ',';
COLON: ':';
DQM: '"';
QM: '\'';
BL: '[';
BR: ']';
CL: '{';
CR: '}';
TRUE: 'true';
FALSE: 'false';
NULL: 'null';
STRING: '"' (ESC | .)*? '"';
NUMBER: INT ('.' DIGITS)? ([Ee][+-]? DIGITS)?;
WS: [ \t\r\n] -> skip;

fragment ESC: '\\' ([bftnr"\\] | 'u' HEX HEX HEX HEX);
fragment HEX: [0-9A-Fa-f];
fragment DIGITS: ([0-9] | [1-9][0-9]+);
fragment INT: '-'? DIGITS;
