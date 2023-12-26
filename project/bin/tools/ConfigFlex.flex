package code;

/* Importamos la libreria de cup para el funcionamiento */
import java_cup.runtime.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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

";" { 
            System.out.print(";");
            nexo("; ");
            return new Symbol(sym.PUNTO_COMA);
            }
    
{espacios} {/*Ignore*/}
"//".* {/*Ignore*/}
. { System.out.print(yytext()); }