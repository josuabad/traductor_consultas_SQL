package code;

/* Importamos la libreria de cup para el funcionamiento */
import java_cup.runtime.*;

%%

/* Integramos el flex con el escanner de cup */
%class Flex
%unicode
%cup

%{
    public static String texto;

    public static String nexo(String palabra) {
        texto = texto + palabra;
        return texto;
    }
%}

/* Algunas definiciones para nuestro lenguaje */
espacios = \r | \n | \r\n | \t | \f
campos = [A-Za-z]+
numeros = [0-9]+

%%

[Dd] [Ee] { 
            System.out.print("FROM");
            nexo("FROM ");
            return new Symbol(sym.FROM);
            }

[Ee] [Ll] [Ee] [Gg] [Ii] [Rr] { 
            System.out.print("SELECT");
            nexo("SELECT ");
            return new Symbol(sym.SELECT);
            }

[Pp] [Aa] [Rr] [Aa] {
            System.out.print("WHERE");
            nexo("WHERE ");
            return new Symbol(sym.WHERE);
            }

"," { 
            System.out.print(", ");
            nexo(", ");
            return new Symbol(sym.COMA);
            }

{campos} | "*" { 
            System.out.print(yytext());
            nexo(yytext() + " ");
            return new Symbol(sym.CAMPO);
            }

{numeros} | "*" { 
            System.out.print(yytext());
            nexo(yytext() + " ");
            return new Symbol(sym.NUMERO);
            }

";" { 
            System.out.print(";");
            nexo("; ");
            return new Symbol(sym.PUNTO_COMA);
            }

"\"" { 
            System.out.print("\"");
            nexo("\"");
            return new Symbol(sym.COMILLA);
            }

">" { 
            System.out.print(">");
            nexo(">");
            return new Symbol(sym.MAYOR);
            }

"<" { 
            System.out.print("<");
            nexo("<");
            return new Symbol(sym.MENOR);
            }

"=" { 
            System.out.print("=");
            nexo("=");
            return new Symbol(sym.IGUAL);
            }

{espacios} {/*Ignore*/}
"//".* {/*Ignore*/}
. { System.out.print(yytext()); }