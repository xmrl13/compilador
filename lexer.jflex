%%

%{

%}


%class AnalisadorLexico
%type void


/* Definições de padrões */
WHITESPACE = [ \t\r\n]+
ID = [a-zA-Z][a-zA-Z0-9]*
NUM = [0-9]+

%%

{WHITESPACE}          { /* ignora espaço em branco */ }

"def"                 { return new Token("def", yytext()); }
"int"                 { return new Token("int", yytext()); }
"if"                  { return new Token("if", yytext()); }
"else"                { return new Token("else", yytext()); }
"print"               { return new Token("print", yytext()); }
"return"              { return new Token("return", yytext()); }

"("                   { return new Token("(", yytext()); }
")"                   { return new Token(")", yytext()); }
"{"                   { return new Token("{", yytext()); }
"}"                   { return new Token("}", yytext()); }
","                   { return new Token(",", yytext()); }
";"                   { return new Token(";", yytext()); }
":="                  { return new Token(":=", yytext()); }

"=="                  { return new Token("==", yytext()); }
"<"                   { return new Token("<", yytext()); }
"<="                  { return new Token("<=", yytext()); }
">"                   { return new Token(">", yytext()); }
">="                  { return new Token(">=", yytext()); }
"<>"                  { return new Token("<>", yytext()); }

"+"                   { return new Token("+", yytext()); }
"-"                   { return new Token("-", yytext()); }
"*"                   { return new Token("*", yytext()); }
"/"                   { return new Token("/", yytext()); }

{ID}                  { return new Token("ID", yytext()); }
{NUM}                 { return new Token("NUM", yytext()); }

.                     { throw new RuntimeException("Caractere inválido: " + yytext()); }

